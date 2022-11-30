package com.example.plantonic.ui.logInSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.OtpVerifyActivity;
import com.example.plantonic.R;
import com.example.plantonic.ui.bottomSheet.BottomSheet;
import com.example.plantonic.ui.bottomSheet.BottomSheetPP;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    EditText phoneNo, name, emailId;
    TextView termsAndConditionBtn,privacyAndPolicyBtn,signINBtn,continueBtn;
    ProgressBar progressBar;

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
                finish();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNo.getText().toString().trim().isEmpty() || name.getText().toString().isEmpty() || emailId.getText().toString().isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Enter your details first",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(view.VISIBLE);
                continueBtn.setVisibility(view.INVISIBLE);
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
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        });
            }
        });
    }

}