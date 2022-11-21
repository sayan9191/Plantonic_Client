package com.example.plantonic.ui.logInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.plantonic.OtpVerifyActivity;
import com.example.plantonic.R;
import com.example.plantonic.ui.bottomSheet.BottomSheet;
import com.example.plantonic.ui.bottomSheet.BottomSheetPP;

public class SignUpActivity extends AppCompatActivity {
    EditText phoneNo, name, password;
    TextView termsAndConditionBtn,privacyAndPolicyBtn,signINBtn,continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        termsAndConditionBtn= findViewById(R.id.termsAndConditionBtn);
        privacyAndPolicyBtn= findViewById(R.id.privacyAndPolicy);
        phoneNo = findViewById(R.id.newPhoneEditTxt);
        name = findViewById(R.id.fullNameEditTxt);
        password = findViewById(R.id.passwordEditTxt);
        signINBtn= findViewById(R.id.btnSignIN);
        continueBtn = findViewById(R.id.logInBtn);

        String phoneNumber = phoneNo.getText().toString();
        termsAndConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet= new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(),"TAG");
            }
        });

        privacyAndPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetPP bottomSheetPP= new BottomSheetPP();
                bottomSheetPP.show(getSupportFragmentManager(),"TAG");
            }
        });

        signINBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, OtpVerifyActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });
    }

}