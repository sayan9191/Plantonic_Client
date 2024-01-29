

package co.in.plantonic.ui.profile;

import android.content.Context;
import android.content.pm.ActivityInfo;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.in.plantonic.BuildConfig;
import co.in.plantonic.firebaseClasses.UserItem;
import co.in.plantonic.ui.orders.YourOrderFragment;
import co.in.plantonic.ui.others.FeedbackFragment;
import co.in.plantonic.ui.others.HelpCenterFragment;
import co.in.plantonic.R;
import co.in.plantonic.ui.profile.editprofile.EditProfileFragment;
import co.in.plantonic.utils.CartUtil;
import co.in.plantonic.utils.OrdersUtil;
import co.in.plantonic.utils.ProfileUtil;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    FrameLayout orderBtn, wishlistBtn, cartBtn, profileBtn, helpCenterBtn, feedbackBtm;
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

        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("Orders")
                        .replace(R.id.fragmentContainerView, new YourOrderFragment())
                        .commit();
                Toast.makeText(getContext(), "Orders", Toast.LENGTH_SHORT).show();
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("Orders")
                        .replace(R.id.fragmentContainerView, new YourOrderFragment())
                        .commit();
                Toast.makeText(getContext(), "Orders", Toast.LENGTH_SHORT).show();
            }
        });

        versionCode.setText(String.valueOf(BuildConfig.VERSION_NAME));
        return view;
    }



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

                if (Objects.equals(OrdersUtil.lastFragment, "orders")){
//                    navController.navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build());
                    OrdersUtil.lastFragment = "";
                }

//                ((HomeActivity)requireActivity()).showBottomNavBar();

            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//    }

}
