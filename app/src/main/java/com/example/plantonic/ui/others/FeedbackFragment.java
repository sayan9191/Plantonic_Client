package com.example.plantonic.ui.others;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.R;
import com.example.plantonic.retrofit.models.feedback.FeedbackResponseModel;
import com.example.plantonic.ui.activity.OrderSummaryActivity;
import com.example.plantonic.ui.dialogbox.LoadingScreen;
import com.example.plantonic.ui.profile.ProfileFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FeedbackFragment extends Fragment {
    ImageView backBtn;
    TextInputEditText feedBack;
    TextView feedbackSubmitBtn;
    View view;
    FeedbackViewModel viewModel;
    Boolean isFeedbackTaken = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);

        viewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        backBtn = view.findViewById(R.id.backBtn);

        feedBack = view.findViewById(R.id.feedBack);
        feedbackSubmitBtn = view.findViewById(R.id.feedbackSubmitBtn);

        viewModel.getFeedbackResponse().observe(getViewLifecycleOwner(), new Observer<FeedbackResponseModel>() {
            @Override
            public void onChanged(FeedbackResponseModel feedbackResponseModel) {
                if (feedbackResponseModel.getStatus().equals("Valid") && !isFeedbackTaken){
                    Toast.makeText(requireContext(), feedbackResponseModel.getDetail(), Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction
                            .setReorderingAllowed(true).addToBackStack("Feedback").replace(R.id.fragmentContainerView, new ThankYouFragment());
                    fragmentTransaction.commit();
                    isFeedbackTaken = true;
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        feedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.requireNonNull(feedBack.getText()).toString().equals("")){
                    viewModel.postFeedback(Objects.requireNonNull(feedBack.getText()).toString());
                    isFeedbackTaken = false;
                }else{
                    Toast.makeText(requireContext(), "Please write some feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!Objects.equals(s, "")) {
                    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    LoadingScreen.Companion.showLoadingDialog(requireContext());
                } else {
                    try {
                        LoadingScreen.Companion.hideLoadingDialog();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
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


    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

    }
}