package com.example.plantonic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantonic.R;

public class PaymentMethodsActivity extends AppCompatActivity {
    LinearLayout cardLayout, gPayLayout, paytmLayout, phonePayLayout, codLayout;
    CheckBox codCheckBox;

    TextView payNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymenth_methods);
        payNowBtn = findViewById(R.id.proceedToPaymentBtn);
        cardLayout = findViewById(R.id.cardLl);
        gPayLayout = findViewById(R.id.gPayLl);
        paytmLayout = findViewById(R.id.paytmLl);
        phonePayLayout = findViewById(R.id.phonePayLl);
        codLayout = findViewById(R.id.coddLl);
        codCheckBox = findViewById(R.id.codCheckBox);

        cardLayout.setOnClickListener(view -> showNotAvailableToast());
        gPayLayout.setOnClickListener(view -> showNotAvailableToast());
        paytmLayout.setOnClickListener(view -> showNotAvailableToast());
        phonePayLayout.setOnClickListener(view -> showNotAvailableToast());
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codCheckBox.isChecked()) {
                    Intent intent = new Intent(PaymentMethodsActivity.this, ThankYouOrderActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showNotAvailableToast() {
        Toast.makeText(getApplicationContext(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();
    }

}