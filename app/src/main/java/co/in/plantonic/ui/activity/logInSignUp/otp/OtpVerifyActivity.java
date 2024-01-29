package co.in.plantonic.ui.activity.logInSignUp.otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import co.in.plantonic.ui.activity.home.HomeActivity;
import co.in.plantonic.R;
import co.in.plantonic.firebaseClasses.UserItem;
import co.in.plantonic.ui.activity.logInSignUp.signup.SignUpActivity;
import co.in.plantonic.utils.StorageUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OtpVerifyActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4, input5, input6;
    TextView mobile,verifyBtn,resendOtpBtn, otpEditNumberBtn;
    ProgressBar progressBar1;
    String VerificationId;

    VerifyOtpViewModel viewModel;

    String fullName, email, phoneNo;

    private final StorageUtil localStorage = StorageUtil.Companion.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        // Initialize storage
        localStorage.setSharedPref(getSharedPreferences("sharedPref", Context.MODE_PRIVATE));

        // initialize the viewModel
        viewModel = new ViewModelProvider(this).get(VerifyOtpViewModel.class);

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);
        input5 = findViewById(R.id.input5);
        input6 = findViewById(R.id.input6);

        progressBar1 = findViewById(R.id.progressbar1);
        verifyBtn = findViewById(R.id.verifyBtn);
        resendOtpBtn = findViewById(R.id.textSendOtp);
        otpEditNumberBtn = findViewById(R.id.otpEditNumberBtn);

        mobile = findViewById(R.id.textMobile);


        VerificationId = getIntent().getStringExtra("verificationId");

        phoneNo = getIntent().getStringExtra("phoneNumber");
        mobile.setText(String.format("%s", phoneNo));

        try {
            fullName = getIntent().getStringExtra("fullName");
            email = getIntent().getStringExtra("email");
        }catch (Exception e){
            e.getStackTrace();
            fullName = null;
            email = null;
        }

        // Start the resend countdown
        startCountdown();

        //Change phone number
        otpEditNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input1.getText().toString().trim().isEmpty()
                        || input2.getText().toString().trim().isEmpty()
                        || input3.getText().toString().trim().isEmpty()
                        || input4.getText().toString().trim().isEmpty()
                        || input5.getText().toString().trim().isEmpty()
                        || input6.getText().toString().trim().isEmpty()
                ){
                    Toast.makeText(OtpVerifyActivity.this, "Please enter valid code",Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = input1.getText().toString() + input2.getText().toString()
                        + input3.getText().toString() + input4.getText().toString() + input5.getText().toString()
                        + input6.getText().toString();


                if (VerificationId!= null){
                    progressBar1.setVisibility(View.VISIBLE);
                    verifyBtn.setVisibility(View.GONE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(VerificationId,code);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        viewModel.checkIfUserExists(Objects.requireNonNull(task.getResult().getUser()).getUid()).observe(OtpVerifyActivity.this, new Observer<Boolean>() {
                                            @Override
                                            public void onChanged(Boolean userExists) {

                                                if (userExists){ // If user already registered

                                                    // verify and get token
                                                    viewModel.getUserToken(task.getResult().getUser().getUid()).observe(OtpVerifyActivity.this, new Observer<String>() {
                                                        @Override
                                                        public void onChanged(String token) {

                                                            progressBar1.setVisibility(View.GONE);
                                                            verifyBtn.setVisibility(View.VISIBLE);

                                                            if (token != null) {
                                                                // save token to local
                                                                localStorage.setToken(token);

                                                                // Existing user again wants to register
                                                                if (email != null && fullName != null){
                                                                    Toast.makeText(OtpVerifyActivity.this, "Account already exists on this number,\nWelcome back to Plantonic", Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(OtpVerifyActivity.this, "Welcome back to Plantonic", Toast.LENGTH_SHORT).show();
                                                                }

                                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                            }else {
                                                                Toast.makeText(OtpVerifyActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                                }else{

                                                    // new user wants to register
                                                    if (email != null && fullName != null){
                                                        // register new user
                                                        viewModel.registerUser(new UserItem(phoneNo, fullName.split(" ")[0], fullName.split(" ")[1], email, task.getResult().getUser().getUid(), "phoneNo"));
                                                    }else {
                                                        // new user wants to login before sign up
                                                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                                                        Toast.makeText(OtpVerifyActivity.this, "User does not exists. Please sign up.", Toast.LENGTH_SHORT).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        progressBar1.setVisibility(View.GONE);
                                        verifyBtn.setVisibility(View.VISIBLE);

                                        Toast.makeText(OtpVerifyActivity.this,"Invalid OTP" + task.getException(),Toast.LENGTH_SHORT).show();
                                        Log.d("Invalid----", task.getException().toString());
                                    }
                                }
                            });
                }
            }
        });
        //resend oto button(it is not working)
        resendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resendOtpBtn.getText().toString().equals("Resend")){
                    startCountdown();
                    resendOtpBtn.setClickable(false);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(getIntent().getStringExtra("phoneNumber"),
                            60L,
                            TimeUnit.SECONDS,
                            OtpVerifyActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(OtpVerifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    VerificationId = newVerificationId;
                                    Toast.makeText(OtpVerifyActivity.this,"OTP sent",Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        setupOTPInputs();

    }


    private void setupOTPInputs(){
        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    input3.requestFocus();
                }else{
                    input1.requestFocus();
                    input1.setSelection(input1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    input4.requestFocus();
                }else{
                    input2.requestFocus();
                    input2.setSelection(input2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    input5.requestFocus();
                }else{
                    input3.requestFocus();
                    input3.setSelection(input3.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    input6.requestFocus();
                }else{
                    input4.requestFocus();
                    input4.setSelection(input4.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()){
                    input5.requestFocus();
                    input5.setSelection(input5.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    private void startCountdown(){
        resendOtpBtn.setClickable(false);

        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendOtpBtn.setText("wait until: " + millisUntilFinished/1000 + " sec");
            }

            @Override
            public void onFinish() {
                resendOtpBtn.setText("Resend");
                resendOtpBtn.setClickable(true);
            }

        };

        timer.start();
    }
}