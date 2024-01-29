package co.`in`.plantonic.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.`in`.plantonic.R
import android.content.Intent
import android.view.View
import co.`in`.plantonic.ui.activity.ThankYouOrderActivity
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import co.`in`.plantonic.databinding.ActivityPaymenthMethodsBinding
import co.`in`.plantonic.firebaseClasses.OrderItem
import co.`in`.plantonic.ui.dialogbox.LoadingScreen

class PaymentMethodsActivity : AppCompatActivity() {

    lateinit var viewModel: PaymentMethodViewModel
    lateinit var binding: ActivityPaymenthMethodsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymenthMethodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PaymentMethodViewModel::class.java]

        val orders : List<OrderItem> = intent.getSerializableExtra("orders") as List<OrderItem>



        binding.proceedToPaymentBtn.setOnClickListener(View.OnClickListener {
            if (binding.codCheckBox.isChecked) {
                viewModel.placeOrder(orders).observe(this@PaymentMethodsActivity
                ) { placeOrderResponseModel ->
                    if (placeOrderResponseModel != null && placeOrderResponseModel.status == "Valid") {

                        startActivity(
                            Intent(
                                this@PaymentMethodsActivity,
                                ThankYouOrderActivity::class.java
                            )
                        );
                    }
                };
            }else{
                Toast.makeText(this@PaymentMethodsActivity, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
        })

        binding.cardLl.setOnClickListener {
            showNotAvailableToast()
        }

        binding.gPayLl.setOnClickListener {
            showNotAvailableToast()
        }

        binding.paytmLl.setOnClickListener {
            showNotAvailableToast()
        }

        binding.phonePayLl.setOnClickListener {
            showNotAvailableToast()
        }

        binding.coddLl.setOnClickListener {
            binding.codCheckBox.isChecked = !binding.codCheckBox.isChecked
        }

        viewModel.errorMessage.observe(this) { s ->
            if (s != "") {
                Toast.makeText(this@PaymentMethodsActivity, s, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                LoadingScreen.showLoadingDialog(this@PaymentMethodsActivity)
            } else {
                try {
                    LoadingScreen.hideLoadingDialog()
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
    }

    private fun showNotAvailableToast() {
        Toast.makeText(applicationContext, getString(R.string.not_available), Toast.LENGTH_SHORT)
            .show()
    }
}