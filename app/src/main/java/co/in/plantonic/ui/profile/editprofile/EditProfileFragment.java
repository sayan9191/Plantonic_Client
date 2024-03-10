package co.in.plantonic.ui.profile.editprofile;

import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.in.plantonic.firebaseClasses.UserItem;
import co.in.plantonic.ui.activity.logInSignUp.login.LoginActivity;
import co.in.plantonic.R;
import co.in.plantonic.ui.activity.splash.SplashScreen;
import co.in.plantonic.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EditProfileFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    EditText firstName, lastName, email, phone;
    TextView backBtn;
    TextView logoutBtn;
    TextView updateProfile;

    View view;
    EditProfileViewModel profileViewModel;

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
        backBtn = view.findViewById(R.id.backBtn);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();


        // Initialize viewModel
        profileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        // GetProfileData
        profileViewModel.getUser(FirebaseAuth.getInstance().getUid()).observe(this.getViewLifecycleOwner(), new Observer<UserItem>() {
            @Override
            public void onChanged(UserItem userItem) {
                firstName.setText(userItem.getFirstName());
                lastName.setText(userItem.getLastName());
                email.setText(userItem.getEmail());
                phone.setText(userItem.getPhoneNo());
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Profile details can't be updated", Toast.LENGTH_SHORT).show();
            }
        });

        //logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(requireContext(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction
                        .setReorderingAllowed(true).addToBackStack("Profile").replace(R.id.fragmentContainerView, new ProfileFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
        }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth == null){
            //user not logged in
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        else {
            //user logged in
            String emailId = firebaseUser.getEmail();
            String fName = firebaseUser.getDisplayName();

            email.setText(emailId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//
//    }


    @Override
    public void onStart() {
        super.onStart();
        // Change status bar color
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.green));
    }

    @Override
    public void onStop() {
        super.onStop();

        // Change status bar color
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.white));
    }

    //backspaced backstack
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