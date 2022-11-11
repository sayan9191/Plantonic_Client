package com.example.plantonic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;


public class EditProfileFragment extends Fragment {
    EditText firstName, lastName, email, phone;
    TextView logoutBtn;
    GoogleSignInClient mGoogleSignInClient;
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
        logoutBtn = view.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // ...
                    case R.id.logoutBtn:
                        signOut();
                        break;
                    // ...
                }
            }
        });

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if( acct !=null)

            {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                int idx = personName.lastIndexOf(' ');
                if (idx == -1)
                    throw new IllegalArgumentException("Only a single name: " + personName);
                String fName = personName.substring(0, idx);
                String lName = personName.substring(idx + 1);
                firstName.setText(fName);
                lastName.setText(lName);
                email.setText(personEmail);
            }
        return view;
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


    void signOut(){
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                requireActivity().finish();
                Intent intent = new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}