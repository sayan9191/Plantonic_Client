package com.example.plantonic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.plantonic.databinding.ActivityHomeBinding;
import com.example.plantonic.ui.cartfav.CartFragment;
import com.example.plantonic.ui.cartfav.FavouriteFragment;
import com.example.plantonic.ui.homeFragment.HomeFragment;
import com.example.plantonic.ui.profile.ProfileFragment;
import com.example.plantonic.utils.CartUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


//        replaceFragment(new HomeFragment());

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            switch (item.getItemId()) {
//
//
//
//                case R.id.profileFragment:
//                    if (Objects.equals(CartUtil.lastFragment, "profile")) {
//                        CartUtil.lastFragment = "";
//                        navController.navigate(R.id.profileFragment, null, new NavOptions.Builder().setPopUpTo(R.id.cartFragment, true).build());
//                    }
//                    break;
//
//            }
//            return true;
//        });
//    }
//
//
//    private void replaceFragment(int id, Fragment fragment) {
//        Fragment f = getSupportFragmentManager().findFragmentById(id);
//        if (f!= null){
//            FragmentManager manager = f.getParentFragmentManager();
//
//            if (manager.getBackStackEntryCount() > 1){
//                manager.popBackStack();
//            }
//        }
    }
}

