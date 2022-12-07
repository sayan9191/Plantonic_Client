

package com.example.plantonic.ui.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.plantonic.ui.cartfav.CartFragment;
import com.example.plantonic.ui.cartfav.FavouriteFragment;
import com.example.plantonic.ui.others.FeedbackFragment;
import com.example.plantonic.ui.others.HelpCenterFragment;
import com.example.plantonic.R;
import com.example.plantonic.ui.profile.editprofile.EditProfileFragment;
import com.example.plantonic.utils.CartUtil;
import com.google.firebase.auth.FirebaseAuth;


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
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true)
                        .addToBackStack("HelpCenter")
                        .replace(R.id.fragmentContainerView, new FavouriteFragment());
                fragmentTransaction.commit();
                Toast.makeText(getContext(), "Your Wishlist!", Toast.LENGTH_SHORT).show();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true)
                        .addToBackStack("cartFragment")
                        .replace(R.id.fragmentContainerView, new CartFragment());
                fragmentTransaction.commit();

                CartUtil.lastFragment = "profile";
                Navigation.findNavController(view).navigate(R.id.cartFragment,null, new NavOptions.Builder().setPopUpTo(R.id.cartFragment, true).build());

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


}
