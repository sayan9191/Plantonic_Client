package com.example.plantonic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ResetPasswordActivity extends AppCompatActivity {
    TextView backBtn;
    LottieAnimationView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        backBtn= findViewById(R.id.btnBack);
        submitBtn= findViewById(R.id.submitBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ResetPasswordActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}