package com.example.plantonic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.plantonic.R
import com.example.plantonic.databinding.ActivityCheckoutBinding
import com.example.plantonic.firebaseClasses.AddressItem
import com.example.plantonic.ui.dialogbox.LoadingScreen.Companion.hideLoadingDialog
import com.example.plantonic.ui.dialogbox.LoadingScreen.Companion.showLoadingDialog
import com.example.plantonic.utils.constants.IntentConstants
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutBinding
    var addressType = "home"
    private lateinit var viewModel: CheckoutActivityViewModel
    var payablePriceLoad: Long? = null
    var isPinCodeChecked = false
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
    private val mobileRegex = Regex("[6-9]\\d{9}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater);
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CheckoutActivityViewModel::class.java]

//        Checkout.preload(getApplicationContext());

        // here try to get the value
        val intent = intent
        payablePriceLoad = intent.getLongExtra("payablePrice", 0L)


        // address type
        binding.homeTypeAddress.setOnClickListener(View.OnClickListener {
            if (addressType != "home") {
                selectHome()
            }
        })
        binding.officeTypeAddress.setOnClickListener(View.OnClickListener {
            if (addressType != "office") {
                selectOffice()
            }
        })


        // add +91
        binding.addressPhoneNo.onFocusChangeListener = OnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                if (binding.addressPhoneNo.text.toString() == "") {
                    binding.addressPhoneNo.setText("+91")
                    binding.addressPhoneNo.setSelection(3)
                }
            } else if (binding.addressPhoneNo.text.toString() == "+91") {
                binding.addressPhoneNo.setText("")
            }
        }


        binding.addressPhoneNo.addTextChangedListener(object : TextWatcher {
            var prevText = ""
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                prevText = charSequence.toString()
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (!binding.addressPhoneNo.text.toString().trim { it <= ' ' }.startsWith("+91")) {
                    binding.addressPhoneNo.setText(prevText)
                    if (3 <= binding.addressPhoneNo.text.toString().trim { it <= ' ' }.length) {
                        binding.addressPhoneNo.setSelection(3)
                    }
                }
            }
        })

        binding.addressPinCode.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(charSequence: Editable?) {
                binding.proceedToPaymentBtn.visibility = View.GONE
                if (charSequence.toString().isNotEmpty()){
                    binding.checkPinCodeBtn.visibility = View.VISIBLE
                }
            }

        })

        viewModel.isPinCodeAvailable.observe(this@CheckoutActivity) { pinCodeAvailableRes ->
            pinCodeAvailableRes?.let {
                if (it.is_delivery_possible) {
                    if (it.location_data.isNotEmpty()){
                        binding.addressState.setText(it.location_data[0].state)
                        binding.addressCity.setText(it.location_data[0].region)
                        binding.addressDistrict.setText(it.location_data[0].dist)
                        binding.proceedToPaymentBtn.visibility = View.VISIBLE
                    }
//                    else{
//                        binding.proceedToPaymentBtn.visibility = View.GONE
//                    }
                }else{
                    Toast.makeText(this@CheckoutActivity, "Sorry, we're not available currently at your area, please try another pincode.", Toast.LENGTH_SHORT).show()
                    binding.proceedToPaymentBtn.visibility = View.GONE
                }
            }
        }

        binding.checkPinCodeBtn.setOnClickListener {
//            isPinCodeChecked = false
            if (binding.addressPinCode.text.toString().length == 6){
                viewModel.checkIfPinCodeAvailable(binding.addressPinCode.text.toString())
            }else{
                Toast.makeText(this@CheckoutActivity, "Invalid pincode.", Toast.LENGTH_SHORT).show()
            }

        }


        // Get saved address
        address
        binding.proceedToPaymentBtn.setOnClickListener(View.OnClickListener {

            if (binding.addressFullName.text.toString() == "") {
                binding.addressFullName.requestFocus()
                Toast.makeText(applicationContext, R.string.full_name_is_missing, Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.addressPhoneNo.text.toString().length != 13 && !binding.addressPhoneNo.text
                    .toString().matches(mobileRegex)) {
                binding.addressFullName.requestFocus()
                Toast.makeText(applicationContext, R.string.phone_number_is_missing, Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.addressPinCode.text.toString() == "") {
                binding.addressPinCode.requestFocus()
                Toast.makeText(applicationContext, R.string.pincode_is_missing, Toast.LENGTH_SHORT).show()
            } else if (binding.addressState.text.toString() == "") {
                binding.addressState.requestFocus()
                Toast.makeText(applicationContext, R.string.state_is_missing, Toast.LENGTH_SHORT).show()
            }else if (binding.addressDistrict.text.toString() == "") {
                binding.addressState.requestFocus()
                Toast.makeText(applicationContext, R.string.district_is_missing, Toast.LENGTH_SHORT).show()
            } else if (binding.addressCity.text.toString() == "") {
                binding.addressCity.requestFocus()
                Toast.makeText(applicationContext, R.string.city_is_missing, Toast.LENGTH_SHORT).show()
            } else if (binding.addressAreaName.text.toString() == "") {
                binding.addressAreaName.requestFocus()
                Toast.makeText(applicationContext, R.string.address_is_missing, Toast.LENGTH_SHORT).show()
                // Check email if entered
            } else if (binding.addressEmail.text.toString() != "" && !binding.addressEmail.text.toString().matches(emailRegex)) {
                binding.addressEmail.text
                Toast.makeText(
                    applicationContext,
                    getString(R.string.enter_valid_email_id),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

//                isPinCodeChecked = false
//                viewModel.checkIfPinCodeAvailable(binding.addressPinCode.text.toString())
//
//                viewModel.isPinCodeAvailable.observe(this@CheckoutActivity) { pinCodeAvailableRes ->
//                    pinCodeAvailableRes?.let {
//                        if (it.is_delivery_possible && !isPinCodeChecked) {
//                            isPinCodeChecked = true
                            saveAddress(
                                AddressItem(
                                    FirebaseAuth.getInstance().uid,
                                    binding.addressFullName.text.toString(),
                                    binding.addressPhoneNo.text.toString(),
                                    binding.addressPinCode.text.toString(),
                                    binding.addressState.text.toString(),
                                    binding.addressCity.text.toString(),
                                    binding.addressAreaName.text.toString(),
                                    addressType,
                                    binding.addressEmail.text.toString(),
                                    binding.addressLandMark.text.toString(),
                                    binding.specialMessage.text.toString()
                                ),
                                binding.addressDistrict.text.toString()
                            )
//                        }else{
//                            Toast.makeText(this@CheckoutActivity, "Sorry, we're not available currently at your area, please try another pincode.", Toast.LENGTH_SHORT).show()
////                            binding.proceedToPaymentBtn.visibility = View.GONE
//                        }
//                    }
//                }

                //                    startPayment(payablePriceLoad);
            }
        })
        viewModel.errorMessage.observe(this) { s ->
            if (s != "") {
                Toast.makeText(this@CheckoutActivity, s, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingDialog(this@CheckoutActivity)
            } else {
                try {
                    hideLoadingDialog()
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
    }

    private fun selectHome() {
        binding.homeTypeAddress.background =
            resources.getDrawable(R.drawable.button_selected, application.theme)
        addressType = "home"
        binding.officeTypeAddress.background =
            resources.getDrawable(R.drawable.button_design, application.theme)
    }

    private fun selectOffice() {
        binding.officeTypeAddress.background =
            resources.getDrawable(R.drawable.button_selected, application.theme)
        addressType = "office"
        binding.homeTypeAddress.background =
            resources.getDrawable(R.drawable.button_design, application.theme)
    }

    private fun saveAddress(addressItem: AddressItem, district: String) {
        binding.addressProgressBar.visibility = View.VISIBLE
        binding.nestedScrollView.isClickable = false
        viewModel.updateAddress(addressItem)
        viewModel.currentAddress.observe(this) { currentAddress ->
            if (currentAddress != null && currentAddress === addressItem) {
                binding.addressProgressBar.visibility = View.GONE
                binding.nestedScrollView.isClickable = true
                val intent = Intent(this@CheckoutActivity, OrderSummaryActivity::class.java)
                intent.putExtra(IntentConstants.DELIVERY_NAME, addressItem.fullName)
                var customerAddress = addressItem.area + ", " + addressItem.city + ", " + district + ", " + addressItem.state + ", Pin - " + addressItem.pinCode
                if (addressItem.landmark != ""){
                    customerAddress += ", Near - ${addressItem.specialInstruction}"
                }
                intent.putExtra(
                    IntentConstants.DELIVERY_ADDRESS, customerAddress
                )
                intent.putExtra(IntentConstants.DELIVERY_PINCODE, addressItem.pinCode)
                intent.putExtra(IntentConstants.ADDRESS_TYPE, addressItem.addressType)
                intent.putExtra(IntentConstants.DELIVERY_PHONE, addressItem.phoneNo)
                intent.putExtra(IntentConstants.DELIVERY_EMAIL, addressItem.email)
//                intent.putExtra(IntentConstants.DELIVERY_LANDMARK, addressItem.landmark)
                intent.putExtra(IntentConstants.DELIVERY_SPECIAL_INSTRUCTION, addressItem.specialInstruction)
                val firstLetterType = addressItem.addressType[0].toString()
                intent.putExtra(
                    IntentConstants.ADDRESS_TYPE,
                    firstLetterType.uppercase(Locale.getDefault()) + addressItem.addressType.substring(
                        1
                    )
                )
                intent.putExtra(IntentConstants.PAY_AMOUNT, payablePriceLoad)
                startActivity(intent)
            }
        }
    }

    // Hide the progress bar
    private val address: Unit
        get() {
            binding.addressProgressBar.visibility = View.VISIBLE
            binding.nestedScrollView.isClickable = false
            viewModel.getAddress(FirebaseAuth.getInstance().uid)
            viewModel.currentAddress.observe(this) { addressItem ->
                if (addressItem != null) {
                    binding.addressFullName.setText(addressItem.fullName)
                    binding.addressPhoneNo.setText(addressItem.phoneNo)
                    binding.addressPinCode.setText(addressItem.pinCode)
//                    binding.addressState.setText(addressItem.state)
//                    binding.addressCity.setText(addressItem.city)
                    binding.addressAreaName.setText(addressItem.area)
                    if (addressItem.addressType == "home") {
                        selectHome()
                    } else {
                        selectOffice()
                    }
                }

                // Hide the progress bar
                binding.addressProgressBar.visibility = View.GONE
                binding.nestedScrollView.isClickable = true
            }
        }

    override fun onRestart() {
        super.onRestart()
        if (binding.addressState.text.toString() != ""){
            binding.proceedToPaymentBtn.visibility = View.VISIBLE
        }
    }
}