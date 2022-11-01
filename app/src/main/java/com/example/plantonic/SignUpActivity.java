package com.example.plantonic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    TextView termsAndConditionBtn,privacyAndPolicyBtn,signINBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        termsAndConditionBtn= findViewById(R.id.termsAndConditionBtn);
        privacyAndPolicyBtn= findViewById(R.id.privacyAndPolicy);
        signINBtn= findViewById(R.id.btnSignIN);


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
                Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}