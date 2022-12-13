package com.example.plantonic.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {
    TextView proceedToPaymentBtn;
    com.google.android.material.textfield.TextInputEditText addressFullName, addressPhoneNo, addressPinCode, addressState, addressCity,addressAreaName;
    String TAG= "checkout";
    private CheckoutActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        proceedToPaymentBtn = findViewById(R.id.proceedToPaymentBtn);
        addressFullName = findViewById(R.id.addressFullName);
        addressPhoneNo = findViewById(R.id.addressPhoneNo);
        addressPinCode = findViewById(R.id.addressPinCode);
        addressState = findViewById(R.id.addressState);
        addressCity = findViewById(R.id.addressCity);
        addressAreaName = findViewById(R.id.addressAreaName);

        viewModel = new ViewModelProvider(this).get(CheckoutActivityViewModel.class);

        Checkout.preload(getApplicationContext());

        // here try to get the value
        Intent intent = getIntent();
        Long payablePriceLoad = intent.getLongExtra("payablePrice",0L);

        proceedToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressFullName.getText().toString().equals("")){
                    addressFullName.requestFocus();
                    Toast.makeText(getApplicationContext(),"Full name is missing",Toast.LENGTH_SHORT).show();
                }
                else if (addressPhoneNo.getText().toString().equals("")){
                    addressFullName.requestFocus();
                    Toast.makeText(getApplicationContext(),"Phone Number is missing",Toast.LENGTH_SHORT).show();
                }
                else if (addressPinCode.getText().toString().equals("")){
                    addressPinCode.requestFocus();
                    Toast.makeText(getApplicationContext(),"Pin code is missing",Toast.LENGTH_SHORT).show();
                }
                else if (addressState.getText().toString().equals("")){
                    addressState.requestFocus();
                    Toast.makeText(getApplicationContext(),"State is missing",Toast.LENGTH_SHORT).show();
                }
                else if (addressCity.getText().toString().equals("")){
                    addressCity.requestFocus();
                    Toast.makeText(getApplicationContext(),"City is missing",Toast.LENGTH_SHORT).show();
                }
                else if (addressAreaName.getText().toString().equals("")){
                    addressAreaName.requestFocus();
                    Toast.makeText(getApplicationContext(),"Address is missing",Toast.LENGTH_SHORT).show();
                }
                else {
//                    startPayment(payablePriceLoad);
                    startActivity(new Intent(CheckoutActivity.this, OrderSummaryActivity.class));
                }
            }
        });

    }
    private void startPayment(Long price) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_krjMSuWqbBTfqs");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.icon);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Plantonic");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#37AC6C");
            options.put("currency", "INR");
            options.put("amount", price*100);//pass amount in currency subunits
            options.put("prefill.email", "plantonic@gmail.com");
            options.put("prefill.contact","8240251373");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG, "onPaymentSuccess: "+s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d(TAG, "onPaymentError: "+s);
    }
}