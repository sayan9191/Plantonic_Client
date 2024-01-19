package com.example.plantonic.ui.activity.logInSignUp.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.R;
import com.example.plantonic.ui.activity.logInSignUp.login.LoginActivity;
import com.example.plantonic.ui.activity.logInSignUp.otp.OtpVerifyActivity;
import com.example.plantonic.ui.bottomSheet.TermsAndConditionBottomSheet;
import com.example.plantonic.ui.bottomSheet.BottomSheetPP;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    EditText phoneNo, name, emailId;
    TextView termsAndConditionBtn,privacyAndPolicyBtn,signINBtn,continueBtn;
    ProgressBar progressBar;
    CheckBox termsCheckBox, privacyPolicyCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        termsAndConditionBtn= findViewById(R.id.termsAndConditionBtn);
        privacyAndPolicyBtn= findViewById(R.id.privacyAndPolicy);
        phoneNo = findViewById(R.id.newPhoneEditTxt);
        name = findViewById(R.id.fullNameEditTxt);
        emailId = findViewById(R.id.emailIdEditTxt);
        signINBtn= findViewById(R.id.btnSignIN);
        continueBtn = findViewById(R.id.logInBtn);
        progressBar = findViewById(R.id.progressbar);
        termsCheckBox = findViewById(R.id.termsAndConditionCheckBox);
        privacyPolicyCheckBox = findViewById(R.id.privacyAndPolicyCheckBox);



        phoneNo.addTextChangedListener(new TextWatcher() {
            String prevText = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!phoneNo.getText().toString().trim().startsWith("+91")) {
                    phoneNo.setText(prevText.toString());
                    if (3 <= phoneNo.getText().toString().trim().length()) {
                        phoneNo.setSelection(3);
                    }
                }
            }
        });

        termsAndConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TermsAndConditionBottomSheet bottomSheet= new TermsAndConditionBottomSheet();
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
                finish();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNo.getText().toString().trim().isEmpty() || name.getText().toString().isEmpty() || emailId.getText().toString().isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please enter valid phone number",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.getText().toString().split(" ").length < 2){
                    Toast.makeText(SignUpActivity.this,"Please enter your full name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!termsCheckBox.isChecked() || !privacyPolicyCheckBox.isChecked()){
                    Toast.makeText(SignUpActivity.this,"Please agree to our privacy policy and terms & conditions",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                continueBtn.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNo.getText().toString(),
                        60L,
                        TimeUnit.SECONDS,
                        SignUpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(view.GONE);
                                continueBtn.setVisibility(view.VISIBLE);
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                               progressBar.setVisibility(view.GONE);
                               continueBtn.setVisibility(view.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(), OtpVerifyActivity.class);
                                intent.putExtra("phoneNumber", phoneNo.getText().toString().trim());
                                intent.putExtra("fullName", name.getText().toString().trim());
                                intent.putExtra("email", emailId.getText().toString().trim());
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        });
            }
        });
    }

}