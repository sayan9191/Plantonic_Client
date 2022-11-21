package com.example.plantonic.ui.others;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantonic.R;
import com.example.plantonic.ui.profile.ProfileFragment;
import com.google.android.material.textfield.TextInputEditText;

public class FeedbackFragment extends Fragment {
    ImageView backBtn;
    TextInputEditText name, emailId, feedBack;
    TextView feedbackSubmitBtn;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);

        backBtn = view.findViewById(R.id.backBtn);
        name = view.findViewById(R.id.name);
        emailId = view.findViewById(R.id.emailId);
        feedBack = view.findViewById(R.id.feedBack);
        feedbackSubmitBtn = view.findViewById(R.id.feedbackSubmitBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction
                        .setReorderingAllowed(true).addToBackStack("Feedback").replace(R.id.fragmentContainerView, new ProfileFragment());
                fragmentTransaction.commit();
            }
        });

        feedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction
                        .setReorderingAllowed(true).addToBackStack("Feedback").replace(R.id.fragmentContainerView, new ThankYouFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
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