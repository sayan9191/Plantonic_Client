package com.example.plantonic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.plantonic.databinding.ActivityHomeBinding;
import com.example.plantonic.ui.cartfav.CartFragment;
import com.example.plantonic.ui.cartfav.FavouriteFragment;
import com.example.plantonic.ui.homeFragment.HomeFragment;
import com.example.plantonic.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


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
//                case R.id.homeFragment:
//                     replaceFragment(R.id.homeFragment, new HomeFragment());
//                    break;
//                case R.id.favouriteFragment:
//                    replaceFragment(R.id.favouriteFragment, (Fragment) new FavouriteFragment());
//                    break;
//                case R.id.cartFragment:
//                    replaceFragment(R.id.cartFragment, new CartFragment());
//                    break;
//                case R.id.profileFragment:
//                    replaceFragment(R.id.profileFragment, new ProfileFragment());
//                    break;
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
