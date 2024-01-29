package co.in.plantonic.ui.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import co.in.plantonic.R;
import co.in.plantonic.databinding.ActivityHomeBinding;
import co.in.plantonic.utils.crypto.EncryptUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    NavController navController;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            Uri data = getIntent().getData();
            Log.d("Got deeplink: ", String.valueOf(data));

            productId = EncryptUtil.Companion.decrypt(String.valueOf(data).substring(26));
        } catch (Exception e){
            e.getStackTrace();
            productId = null;
        }



        if (savedInstanceState == null) {
            navController = Navigation.findNavController(this, R.id.fragmentContainerView);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }




//        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()



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

//    @Override
//    public void onResume() {
//        super.onResume();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try{
            navController.handleDeepLink(intent);
        } catch (Exception e){
            e.getStackTrace();
        }

    }

    public void hideBottomNavBar(){
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavBar(){
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }
}

