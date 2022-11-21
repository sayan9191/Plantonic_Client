package com.example.plantonic.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.plantonic.ui.logInSignUp.LoginActivity;
import com.example.plantonic.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class EditProfileFragment extends Fragment {
    EditText firstName, lastName, email, phone;
    TextView logoutBtn;
    TextView updateProfile;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        updateProfile = view.findViewById(R.id.updateProfile);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account!= null){
            String personName = account.getDisplayName();
            email.setText(account.getEmail());
            int idx = personName.lastIndexOf(' ');
            if (idx == -1)
                throw new IllegalArgumentException("Only a single name: " + personName);
            String fName = personName.substring(0, idx);
            String lName = personName.substring(idx + 1);
            firstName.setText(fName);
            lastName.setText(lName);
        }
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        return view;
        }
    void signOut(){
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }


    @Override
        public void onAttach (@NonNull Context context){
            super.onAttach(context);
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.popBackStackImmediate();
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        }


}