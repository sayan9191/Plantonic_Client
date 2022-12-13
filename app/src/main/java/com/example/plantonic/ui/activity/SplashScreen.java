package com.example.plantonic.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.plantonic.R;
import com.example.plantonic.ui.logInSignUp.login.LoginActivity;
import com.example.plantonic.ui.logInSignUp.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    MotionLayout motionLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        motionLayout = findViewById(R.id.motionLayout);

        // Initialize viewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {


            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                checkUser();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }


    private void checkUser() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            loginViewModel.checkIfUserExists(FirebaseAuth.getInstance().getUid()).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean userExists) {

                    if (userExists) {
                        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }


                }
            });
        } else {


            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();


        }
    }
}