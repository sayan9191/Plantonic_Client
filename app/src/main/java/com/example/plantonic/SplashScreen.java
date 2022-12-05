package com.example.plantonic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.plantonic.ui.logInSignUp.login.LoginActivity;
import com.example.plantonic.ui.logInSignUp.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    MotionLayout motionLayout;

    Boolean userExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        motionLayout = findViewById(R.id.motionLayout);

        // Initialize viewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        checkUser();
        motionLayout.transitionToEnd();

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

                if (userExist){
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }


    private void checkUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!= null){

            loginViewModel.checkIfUserExists(firebaseUser.getUid()).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean userExists) {
                    if (userExists){

                        userExist = true;
                        motionLayout.transitionToStart();
                    }else {

                        userExist = false;
                        motionLayout.transitionToStart();
                    }
                }
            });
        }else {

            userExist = false;
            motionLayout.transitionToStart();
        }
    }
}