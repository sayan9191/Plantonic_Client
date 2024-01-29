package co.in.plantonic.ui.activity.logInSignUp.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import co.in.plantonic.R;
import co.in.plantonic.firebaseClasses.UserItem;
import co.in.plantonic.ui.activity.home.HomeActivity;
import co.in.plantonic.ui.activity.logInSignUp.otp.OtpVerifyActivity;
import co.in.plantonic.ui.activity.logInSignUp.signup.SignUpActivity;
import co.in.plantonic.utils.StorageUtil;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText phoneNumber;
    TextView logInBtn, signUpBtn;
    ConstraintLayout googleSignInBtn;
    ProgressBar progressBar;
    private static final int RC_SIGN_IN = 1000;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    LoginViewModel viewModel;
    private final StorageUtil localStorage = StorageUtil.Companion.getInstance();


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

        // Initialize storage
        localStorage.setSharedPref(getSharedPreferences("sharedPref", Context.MODE_PRIVATE));


        // Initialize view model
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // add +91
        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    if (phoneNumber.getText().toString().equals("")) {
                        phoneNumber.setText("+91");
                        phoneNumber.setSelection(3);
                    }
                } else if (phoneNumber.getText().toString().equals("+91")) {
                    phoneNumber.setText("");
                }
            }
        });


        phoneNumber.addTextChangedListener(new TextWatcher() {
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
                if (!phoneNumber.getText().toString().trim().startsWith("+91")) {
                    phoneNumber.setText(prevText.toString());
                    if (3 <= phoneNumber.getText().toString().trim().length()) {
                        phoneNumber.setSelection(3);
                    }
                }
            }
        });


        //configure Google signIn
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_google_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

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

                if (phoneNumber.getText().toString().trim().isEmpty()
                        || phoneNumber.getText().toString().trim().length() < 13
                        || !phoneNumber.getText().toString().trim().startsWith("+91")) {
                    Toast.makeText(LoginActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
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
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber.getText().toString().trim(),
                60L,
                TimeUnit.SECONDS,
                LoginActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

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

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Google SignIn intent Result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            } catch (Exception exception) {
                Log.d(TAG, "onActivityResult:" + exception.getMessage());
            }
        }
    }

    private boolean isNew = false;

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
                        // user info
                        if (firebaseUser != null) {
                            String fullName = firebaseUser.getDisplayName();
                            String uid = firebaseUser.getUid();
                            String email = firebaseUser.getEmail();

                            Log.d(TAG, "onSuccess: Email...\n" + email);
                            Log.d(TAG, "onSuccess:  UID...\n" + uid);

                            // check new user or existing user
                            viewModel.checkIfUserExists(uid).observe(LoginActivity.this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean isExisting) {

                                    // If existing verified user
                                    if (isExisting) {
                                        if (fullName != null) {
                                            if (!isNew)
                                                Toast.makeText(LoginActivity.this, "Welcome back,\n" + fullName.split(" ")[0], Toast.LENGTH_SHORT).show();
                                            else{
                                                Toast.makeText(LoginActivity.this, "Account created as,\n"+ fullName, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        viewModel.getUserToken(uid).observe(LoginActivity.this, new Observer<String>() {
                                            @Override
                                            public void onChanged(String token) {
                                                if (token != null) {
                                                    // save token to local
                                                    localStorage.setToken(token);
                                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    } else {
//                                        if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()){
                                        //new account created
                                        isNew = true;
                                        Log.d(TAG, "onSuccess: Account created...\n" + email);

                                        String firstName = "";
                                        String lastName = "";

                                        if (fullName != null) {
                                            firstName = fullName.split(" ")[0];
                                            if (fullName.split(" ").length > 1) {
                                                lastName = fullName.split(" ")[1];
                                            }
                                        }
                                        String userPhoneNumber = "";
                                        if (firebaseUser.getPhoneNumber() != null) {
                                            userPhoneNumber = firebaseUser.getPhoneNumber();
                                        }

                                        Objects.requireNonNull(viewModel.registerUser(new UserItem(userPhoneNumber, firstName, lastName, email, uid, "google")));
                                                /*.observe(LoginActivity.this, new Observer<Boolean>() {
                                                @Override
                                                public void onChanged(Boolean isUserCreated) {
                                                    if (isUserCreated) {
                                                        Toast.makeText(LoginActivity.this, "Account created as,\n"+ fullName, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });*/
                                        /*}
                                        else{
                                            //existing user
                                            Log.d(TAG,"onSuccess: Existing user...\n"+email);
                                            assert fullName != null;
                                            Toast.makeText(LoginActivity.this, "Welcome back,\n"+fullName.split(" ")[0], Toast.LENGTH_SHORT).show();
                                        }*/
                                    }
                                }
                            });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Logged in failed" + e.getMessage());
                    }
                });
    }
}

