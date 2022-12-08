

package com.example.plantonic.ui.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantonic.BuildConfig;
import com.example.plantonic.firebaseClasses.UserItem;
import com.example.plantonic.ui.others.FeedbackFragment;
import com.example.plantonic.ui.others.HelpCenterFragment;
import com.example.plantonic.R;
import com.example.plantonic.ui.profile.editprofile.EditProfileFragment;
import com.example.plantonic.utils.CartUtil;
import com.example.plantonic.utils.FavUtil;
import com.example.plantonic.utils.constants.HomeUtil;
import com.example.plantonic.utils.constants.ProfileUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    ImageView orderBtn, wishlistBtn, cartBtn, profileBtn, helpCenterBtn, feedbackBtm;
    TextView versionCode, userName;
    View view;

    ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        orderBtn = view.findViewById(R.id.orderViewBtn);
        wishlistBtn = view.findViewById(R.id.wishlistBtn);
        cartBtn = view.findViewById(R.id.cartBtn);
        profileBtn = view.findViewById(R.id.profileBtn);
        helpCenterBtn = view.findViewById(R.id.helpCenterBtn);
        feedbackBtm = view.findViewById(R.id.feedbackBtn);
        versionCode = view.findViewById(R.id.versionCode);
        userName = view.findViewById(R.id.userNameGreetings);

        // Initialize viewModel
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Get Current User Name
        profileViewModel.getUser(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<UserItem>() {
            @Override
            public void onChanged(UserItem userItem) {
                if (userItem != null){
                    userName.setText(userItem.getFirstName() + " " + userItem.getLastName());
                }
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartUtil.lastFragment = "profile";
                ProfileUtil.lastFragment = "fav";

                Navigation.findNavController(ProfileFragment.this.view).navigate(R.id.favouriteFragment,null, new NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build());
                Toast.makeText(getContext(), "Your Wishlist!", Toast.LENGTH_SHORT).show();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartUtil.lastFragment = "profile";
                ProfileUtil.lastFragment = "cart";

                Navigation.findNavController(ProfileFragment.this.view).navigate(R.id.cartFragment,null, new NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build());
                Toast.makeText(getContext(), "Your Cart Items!", Toast.LENGTH_SHORT).show();
            }
        });
        helpCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("HelpCenter")
                        .replace(R.id.fragmentContainerView, new HelpCenterFragment())
                        .commit();
                Toast.makeText(getContext(), "Help Center", Toast.LENGTH_SHORT).show();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("Profile")
                        .replace(R.id.fragmentContainerView, new EditProfileFragment())
                        .commit();
                Toast.makeText(getContext(), "Profile", Toast.LENGTH_SHORT).show();

            }
        });

        feedbackBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("Feedback")
                        .replace(R.id.fragmentContainerView, new FeedbackFragment())
                        .commit();
                Toast.makeText(getContext(), "Feedback", Toast.LENGTH_SHORT).show();

            }
        });

        versionCode.setText(String.valueOf(BuildConfig.VERSION_NAME));
        return view;
    }


    //backspaced backstack

    //backspaced backstack
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                FragmentManager manager = getActivity().getSupportFragmentManager();

                while(manager.getBackStackEntryCount() > 1 && !Objects.equals(ProfileUtil.lastFragment, "")) {
                    manager.popBackStackImmediate();
                    ProfileUtil.lastFragment = "";
                }

                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                navController.navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build());
                manager.popBackStackImmediate();

            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }


}
