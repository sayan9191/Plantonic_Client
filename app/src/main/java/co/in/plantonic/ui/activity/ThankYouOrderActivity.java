package co.in.plantonic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import co.in.plantonic.databinding.ActivityThankYouOrderBinding;
import co.in.plantonic.ui.activity.home.HomeActivity;
import co.in.plantonic.ui.activity.splash.SplashScreen;

public class ThankYouOrderActivity extends AppCompatActivity {
    ActivityThankYouOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThankYouOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouOrderActivity.this, SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        ThankYouOrderActivity.this.finish();
    }
}