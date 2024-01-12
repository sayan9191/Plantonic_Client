package com.example.plantonic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.AddressItem;
import com.example.plantonic.retrofit.models.pincode.PinCodeAvailableResponseModel;
import com.example.plantonic.ui.dialogbox.LoadingScreen;
import com.example.plantonic.utils.constants.IntentConstants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {
    TextView proceedToPaymentBtn, homeTypeAddress, officeTypeAddress;
    com.google.android.material.textfield.TextInputEditText addressFullName, addressPhoneNo, addressPinCode, addressState, addressCity, addressAreaName, addressEmailId;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;

    String addressType = "home";
    private CheckoutActivityViewModel viewModel;
    Long payablePriceLoad;
    String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    String mobileRegex = "[6-9]\\d{9}";


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
        addressEmailId = findViewById(R.id.addressEmailId);


        viewModel = new ViewModelProvider(this).get(CheckoutActivityViewModel.class);
        // here try to get the value
        Intent intent = getIntent();
        payablePriceLoad = intent.getLongExtra("payablePrice", 0L);


        // address type
        homeTypeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(addressType, "home")) {
                    selectHome();
                }
            }
        });

        officeTypeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(addressType, "office")) {
                    selectOffice();
                }
            }
        });

        // Get saved address
        getAddress();


        proceedToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(addressFullName.getText()).toString().equals("")) {
                    addressFullName.requestFocus();
                    Toast.makeText(getApplicationContext(), getString(R.string.full_name_is_missing), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(addressPhoneNo.getText()).toString().equals("")) {
                    addressFullName.requestFocus();
                    Toast.makeText(getApplicationContext(), getString(R.string.phone_number_is_missing), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(addressPinCode.getText()).toString().equals("")) {
                    addressPinCode.requestFocus();
                    Toast.makeText(getApplicationContext(), getString(R.string.pincode_is_missing), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(addressState.getText()).toString().equals("")) {
                    addressState.requestFocus();
                    Toast.makeText(getApplicationContext(), getString(R.string.state_is_missing), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(addressCity.getText()).toString().equals("")) {
                    boolean b;
                    if (addressCity.requestFocus()) b = true;
                    else b = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.city_is_missing), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(addressAreaName.getText()).toString().equals("")) {
                    addressAreaName.requestFocus();
                    Toast.makeText(getApplicationContext(), getString(R.string.address_is_missing), Toast.LENGTH_SHORT).show();
                } else if (!(addressEmailId.getText().toString().matches(emailRegex))) {
                    addressEmailId.requestFocus();
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_email_id), Toast.LENGTH_SHORT).show();
                } else {
                    // Check phone number
                    if (addressPhoneNo.getText().toString().length() != 10 && addressPhoneNo.getText().toString().matches(mobileRegex)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_mobile_no), Toast.LENGTH_SHORT).show();
                        addressPhoneNo.requestFocus();
                    }

                    viewModel.checkIfPinCodeAvailable(addressPinCode.getText().toString());
                    viewModel.isPinCodeAvailable.observe(CheckoutActivity.this, new Observer<PinCodeAvailableResponseModel>() {
                        @Override
                        public void onChanged(PinCodeAvailableResponseModel pinCodeAvailableRes) {
                            if (pinCodeAvailableRes != null && pinCodeAvailableRes.is_delivery_possible()) {
                                saveAddress(new AddressItem(FirebaseAuth.getInstance().getUid(), addressFullName.getText().toString(), addressPhoneNo.getText().toString(),
                                        addressPinCode.getText().toString(), addressState.getText().toString(), addressCity.getText().toString(),
                                        addressAreaName.getText().toString(), addressType));
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.UNABLE_TO_FETCH_PIN_CODE), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    startPayment(payablePriceLoad);


                }
            }
        });

        viewModel.errorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!Objects.equals(s, "")) {
                    Toast.makeText(CheckoutActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    LoadingScreen.Companion.showLoadingDialog(CheckoutActivity.this);
                } else {
                    try {
                        LoadingScreen.Companion.hideLoadingDialog();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        });
    }

    private void selectHome() {
        homeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_selected, getApplication().getTheme()));
        addressType = "home";
        officeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_design, getApplication().getTheme()));
    }

    private void selectOffice() {
        officeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_selected, getApplication().getTheme()));
        addressType = "office";
        homeTypeAddress.setBackground(getResources().getDrawable(R.drawable.button_design, getApplication().getTheme()));
    }


    private void saveAddress(AddressItem addressItem) {
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setClickable(false);

        viewModel.updateAddress(addressItem);
        viewModel.currentAddress.observe(this, new Observer<AddressItem>() {
            @Override
            public void onChanged(AddressItem currentAddress) {
                if (currentAddress != null && currentAddress == addressItem) {
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

    private void getAddress() {
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setClickable(false);

        viewModel.getAddress(FirebaseAuth.getInstance().getUid());
        viewModel.currentAddress.observe(this, new Observer<AddressItem>() {
            @Override
            public void onChanged(AddressItem addressItem) {
                if (addressItem != null) {
                    addressFullName.setText(addressItem.getFullName());
                    addressPhoneNo.setText(addressItem.getPhoneNo());
                    addressPinCode.setText(addressItem.getPinCode());
                    addressState.setText(addressItem.getState());
                    addressCity.setText(addressItem.getCity());
                    addressAreaName.setText(addressItem.getArea());

                    if (Objects.equals(addressItem.getAddressType(), "home")) {
                        selectHome();
                    } else {
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