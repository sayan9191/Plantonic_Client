package com.example.plantonic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password1;
    int RC_SIGN_IN= 0;
    GoogleSignInClient mGoogleSignInClient;
    TextView logInBtn,forgotPasswordBtn,signInBtn;
    com.google.android.gms.common.SignInButton signInGoogleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logInBtn= findViewById(R.id.logInBtn);
        signInBtn= findViewById(R.id.btnSignIN);
        emailId = findViewById(R.id.emailEditTxt);
        password1 = findViewById(R.id.passwordEditTxt);
        forgotPasswordBtn= findViewById(R.id.forgotPassword);
        signInGoogleBtn= findViewById(R.id.btnSignInGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString().trim();
                String password = password1.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//                if (email.equals(emailPattern) && password.length()>=8){
//                    Intent intent= new Intent(getApplicationContext(),HomeActivity.class);
//                    startActivity(intent);
//                }
//                else if (email.equals("") && password.length()==0){
//                    Toast.makeText(getApplicationContext(),"Enter your details first", Toast.LENGTH_SHORT).show();
//                }
//                else if(!email.equals("") && !email.equals(emailPattern)){
//                    Toast.makeText(getApplicationContext(),"Enter valid email id", Toast.LENGTH_SHORT).show();
//                }
//                else if (password.length() < 8){
//                    Toast.makeText(getApplicationContext(),"Enter your password", Toast.LENGTH_SHORT).show();
//                }

                if (email.equals("") && password.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter your details", Toast.LENGTH_SHORT).show();
                }else if (email.equals("") || !email.matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Enter valid email", Toast.LENGTH_SHORT).show();
                }else if (password.length() < 8){
                    Toast.makeText(getApplicationContext(),"Enter your password", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent= new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                }

            }
        });
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });
        signInGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnSignInGoogle:
                        signIn();
                        break;
                    // ...
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void navigateUpToHomeActivity() {
        Intent intent= new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}