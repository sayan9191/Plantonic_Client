package com.example.plantonic.ui.logInSignUp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.HomeActivity;
import com.example.plantonic.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    TextView signInGoogleBtn;

    EditText phoneNumber, password1;
    TextView logInBtn, forgotPasswordBtn, signInBtn;

    String TAG = "LoginActivityGoogle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInGoogleBtn = findViewById(R.id.btnSignInGoogle);
        logInBtn = findViewById(R.id.logInBtn);
        signInBtn = findViewById(R.id.btnSignIN);
        phoneNumber = findViewById(R.id.emailEditTxt);
        password1 = findViewById(R.id.passwordEditTxt);
        forgotPasswordBtn = findViewById(R.id.forgotPassword);


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_google_web_client_id))
                .requestEmail()
                .build();
//        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken().requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            navigateHomeActivity();
        }


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = phoneNumber.getText().toString().trim();
                String password = password1.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (email.equals("") && password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter your details", Toast.LENGTH_SHORT).show();
                } else if (email.equals("") || !email.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }

            }
        });
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        signInGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }


    void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        loginWithGoogleActivityResultLauncher.launch(signInIntent);
    }



    ActivityResultLauncher<Intent> loginWithGoogleActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("tttt", "OnActivityResult");

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        Log.d("tttt", "OnActivityResult Ok");
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }else {
                        Log.d("tttt", "OnActivityResult " + result);
                    }
                }
            });




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
//
//            Log.d(TAG, "---OnActivityResult Ok");
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            if (task != null){
//                handleSignInResult(task);
//            }else{
//                Log.d(TAG, "---Task = null");
//            }
//
//        }
//    }

    void navigateHomeActivity() {
        finish();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount account = null;
        try {
            account = completedTask.getResult(ApiException.class);

            if (account != null) {
                Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_LONG).show();
                // Signed in successfully, show authenticated UI.
                //    val personName: String = account.displayName!!
//                val personGivenName: String = account.givenName!!
//                val personFamilyName: String = account.familyName!!
//                val personEmail: String = account.email!!
//                val personId: String = account.id!!
//                val personPhoto: Uri = account.photoUrl!!

                String token = account.getIdToken();
                if (token != null) {
                    firebaseAuthWithGoogle(token);
                }
            }

        } catch (ApiException e) {
            e.printStackTrace();
        }


    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            navigateHomeActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}

