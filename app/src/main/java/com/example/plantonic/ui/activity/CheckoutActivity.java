package com.example.plantonic.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.AddressItem;
import com.example.plantonic.utils.constants.IntentConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {
    TextView proceedToPaymentBtn, homeTypeAddress, officeTypeAddress;
    com.google.android.material.textfield.TextInputEditText addressFullName, addressPhoneNo, addressPinCode, addressState, addressCity,addressAreaName;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;

    String addressType = "home";
    private CheckoutActivityViewModel viewModel;
    Long payablePriceLoad;

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
        homeTypeAddress = findViewById(R.id.homeTypeAddress);
        officeTypeAddress = findViewById(R.id.officeTypeAddress);
        progressBar = findViewById(R.id.addressProgressBar);
        nestedScrollView = findViewById(R.id.nestedScrollView);


        viewModel = new ViewModelProvider(this).get(CheckoutActivityViewModel.class);

//        Checkout.preload(getApplicationContext());

        // here try to get the value
        Intent intent = getIntent();
        payablePriceLoad = intent.getLongExtra("payablePrice",0L);


        // address type
        homeTypeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(addressType, "home")){
                    selectHome();
                }
            }
        });

        officeTypeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(addressType, "office")){
                    selectOffice();
                }
            }
        });

        // Get saved address
        getAddress();


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
                    saveAddress(new AddressItem(FirebaseAuth.getInstance().getUid(), addressFullName.getText().toString(), addressPhoneNo.getText().toString(),
                            addressPinCode.getText().toString(), addressState.getText().toString(), addressCity.getText().toString(),
                            addressAreaName.getText().toString(), addressType));

                }
            }
        });

    }

    private void selectHome(){
        homeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_selected, getApplication().getTheme()));
        addressType = "home";
        officeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_design, getApplication().getTheme()));
    }

    private void selectOffice(){
        officeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_selected, getApplication().getTheme()));
        addressType = "office";
        homeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_design, getApplication().getTheme()));
    }


    private void saveAddress(AddressItem addressItem){
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setClickable(false);

        viewModel.updateAddress(addressItem);
        viewModel.currentAddress.observe(this, new Observer<AddressItem>() {
            @Override
            public void onChanged(AddressItem currentAddress) {
                if (currentAddress != null && currentAddress == addressItem){
                    progressBar.setVisibility(View.GONE);
                    nestedScrollView.setClickable(true);

                    Intent intent = new Intent(CheckoutActivity.this, OrderSummaryActivity.class);
                    intent.putExtra(IntentConstants.DELIVERY_NAME, addressItem.getFullName());
                    intent.putExtra(IntentConstants.DELIVERY_ADDRESS, addressItem.getArea() + ", "
                                    + addressItem.getCity() + ", " + addressItem.getState() + ", Pin - "
                                    + addressItem.getPinCode());

                    intent.putExtra(IntentConstants.ADDRESS_TYPE, addressItem.getAddressType());
                    intent.putExtra(IntentConstants.DELIVERY_PHONE, addressItem.getPhoneNo());

                    String firstLetterType = String.valueOf(addressItem.getAddressType().charAt(0));

                    intent.putExtra(IntentConstants.ADDRESS_TYPE, firstLetterType.toUpperCase() + addressItem.getAddressType().substring(1));

                    intent.putExtra(IntentConstants.PAY_AMOUNT, payablePriceLoad);

                    startActivity(intent);
                }
            }
        });
    }

    private void getAddress(){
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setClickable(false);

        viewModel.getAddress(FirebaseAuth.getInstance().getUid());
        viewModel.currentAddress.observe(this, new Observer<AddressItem>() {
            @Override
            public void onChanged(AddressItem addressItem) {
                if (addressItem != null){
                    addressFullName.setText(addressItem.getFullName());
                    addressPhoneNo.setText(addressItem.getPhoneNo());
                    addressPinCode.setText(addressItem.getPinCode());
                    addressState.setText(addressItem.getState());
                    addressCity.setText(addressItem.getCity());
                    addressAreaName.setText(addressItem.getArea());

                    if (Objects.equals(addressItem.getAddressType(), "home")){
                        selectHome();
                    }else {
                        selectOffice();
                    }
                }

                // Hide the progress bar
                progressBar.setVisibility(View.GONE);
                nestedScrollView.setClickable(true);


            }
        });
    }



}