package com.example.plantonic.ui.logInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.plantonic.ui.activity.HomeActivity;
import com.example.plantonic.R;

public class ResetPasswordActivity extends AppCompatActivity {
    TextView backBtn;
    EditText password, confirmPassword;
    LottieAnimationView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        password = findViewById(R.id.resetPasswordTxt);
        confirmPassword = findViewById(R.id.confirmPasswordTxt);
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
                String password1 = password.getText().toString().trim();
                String confirmPassword1 = confirmPassword.getText().toString().trim();
                if (password1.length() >= 8 && confirmPassword1.equals(password1)){
                    Intent intent= new Intent(ResetPasswordActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else if (password1.equals("") ){
                    Toast.makeText(getApplicationContext(),"Enter New password first", Toast.LENGTH_SHORT).show();
                }
                else if (confirmPassword1.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter confirm password first", Toast.LENGTH_SHORT).show();
                }
                else if (password1.length()<8){
                    Toast.makeText(getApplicationContext(),"Password Length must be 8", Toast.LENGTH_SHORT).show();
                }
                else if (!password1.equals(confirmPassword1)){
                    Toast.makeText(getApplicationContext(),"Password not matches", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}