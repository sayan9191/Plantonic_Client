package com.example.plantonic.ui.activity

import com.example.plantonic.ui.dialogbox.LoadingScreen.Companion.showLoadingDialog
import com.example.plantonic.ui.dialogbox.LoadingScreen.Companion.hideLoadingDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantonic.R
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.plantonic.databinding.ActivityCheckoutBinding

import com.example.plantonic.firebaseClasses.AddressItem
import com.google.firebase.auth.FirebaseAuth

import com.example.plantonic.utils.constants.IntentConstants
import java.lang.Exception
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutBinding
    var addressType = "home"
    private lateinit var viewModel: CheckoutActivityViewModel
    var payablePriceLoad: Long? = null
    var isPinCodeChecked = false
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

        // Get saved address
        address
        binding.proceedToPaymentBtn.setOnClickListener(View.OnClickListener {
            if (binding.addressFullName.text.toString() == "") {
                binding.addressFullName.requestFocus()
                Toast.makeText(applicationContext, "Full name is missing", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.addressPhoneNo.text.toString() == "") {
                binding.addressFullName.requestFocus()
                Toast.makeText(applicationContext, "Phone Number is missing", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.addressPinCode.text.toString() == "") {
                binding.addressPinCode.requestFocus()
                Toast.makeText(applicationContext, "Pin code is missing", Toast.LENGTH_SHORT).show()
            } else if (binding.addressState.text.toString() == "") {
                binding.addressState.requestFocus()
                Toast.makeText(applicationContext, "State is missing", Toast.LENGTH_SHORT).show()
            } else if (binding.addressCity.text.toString() == "") {
                binding.addressCity.requestFocus()
                Toast.makeText(applicationContext, "City is missing", Toast.LENGTH_SHORT).show()
            } else if (binding.addressAreaName.text.toString() == "") {
                binding.addressAreaName.requestFocus()
                Toast.makeText(applicationContext, "Address is missing", Toast.LENGTH_SHORT).show()
            } else {
                // Check phone number

                // Check email if entered
                isPinCodeChecked = false
                viewModel.checkIfPinCodeAvailable(binding.addressPinCode.text.toString())
                viewModel.isPinCodeAvailable.observe(this@CheckoutActivity) { pinCodeAvailableRes ->
                    pinCodeAvailableRes?.let {
                        if (it.is_delivery_possible && !isPinCodeChecked) {
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
                                        binding.addressEmail.text.toString()
                                    )
                                )
                                isPinCodeChecked = true
                        }
                    }
                }
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

    private fun saveAddress(addressItem: AddressItem) {
        binding.addressProgressBar.visibility = View.VISIBLE
        binding.nestedScrollView.isClickable = false
        viewModel.updateAddress(addressItem)
        viewModel.currentAddress.observe(this) { currentAddress ->
            if (currentAddress != null && currentAddress === addressItem) {
                binding.addressProgressBar.visibility = View.GONE
                binding.nestedScrollView.isClickable = true
                val intent = Intent(this@CheckoutActivity, OrderSummaryActivity::class.java)
                intent.putExtra(IntentConstants.DELIVERY_NAME, addressItem.fullName)
                intent.putExtra(
                    IntentConstants.DELIVERY_ADDRESS, addressItem.area + ", "
                            + addressItem.city + ", " + addressItem.state + ", Pin - "
                            + addressItem.pinCode
                )
                intent.putExtra(IntentConstants.DELIVERY_PINCODE, addressItem.pinCode)
                intent.putExtra(IntentConstants.ADDRESS_TYPE, addressItem.addressType)
                intent.putExtra(IntentConstants.DELIVERY_PHONE, addressItem.phoneNo)
                intent.putExtra(IntentConstants.DELIVERY_EMAIL, addressItem.email)
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
                    binding.addressState.setText(addressItem.state)
                    binding.addressCity.setText(addressItem.city)
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
}