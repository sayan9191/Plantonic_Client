package com.example.plantonic.ui.logInSignUp.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.R;
import com.example.plantonic.ui.logInSignUp.otp.OtpVerifyActivity;
import com.example.plantonic.ui.logInSignUp.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText phoneNumber;
    TextView logInBtn, signUpBtn;
    TextView googleSignInBtn;
    ProgressBar progressBar;
    private static final int RC_SIGN_IN= 1000;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;


    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumber = findViewById(R.id.phoneEdTxt);
        logInBtn = findViewById(R.id.logInBtn);
        signUpBtn = findViewById(R.id.btnSignIN);
        googleSignInBtn = findViewById(R.id.btnSignInGoogle);
        progressBar = findViewById(R.id.progressbar);




        //configure Google signIn
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_google_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
//        checkUser();

        //google button
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: begin google signIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phoneNumber.getText().toString().trim().isEmpty() ){
                    Toast.makeText(LoginActivity.this,"Enter your details",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                logInBtn.setVisibility(View.INVISIBLE);
                sendCode(phoneNumber);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendCode(EditText phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phoneNumber.getText().toString().trim(),
                60L,
                TimeUnit.SECONDS,
                LoginActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        logInBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        logInBtn.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getApplicationContext(), OtpVerifyActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber.getText().toString().trim());
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                        finish();
                    }
                });
    }

//    private void checkUser() {
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser!= null){
//
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            finish();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK){
            Log.d(TAG,"onActivityResult: Google SignIn intent Result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception exception){
                Log.d(TAG,"onActivityResult:"+ exception.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "onSuccess: Logged In");

                        //get logged in user
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail(); // user info

                        Log.d(TAG,"onSuccess: Email...\n"+email);
                        Log.d(TAG,"onSuccess:  UID...\n"+uid);

                        // check new user or existing user
                        if (authResult.getAdditionalUserInfo().isNewUser()){
                            //new account created
                            Log.d(TAG,"onSuccess: Account created...\n"+email);
                            Toast.makeText(LoginActivity.this, "Account created...\n"+email, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //existing user
                            Log.d(TAG,"onSuccess: Existing user...\n"+email);
                            Toast.makeText(LoginActivity.this, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: Logged in failed" +e.getMessage());
                    }
                });
    }
}

