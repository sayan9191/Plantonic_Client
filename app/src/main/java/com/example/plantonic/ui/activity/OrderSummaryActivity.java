package com.example.plantonic.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.plantonic.Adapter.OrderSummaryRVAdapter;
import com.example.plantonic.databinding.ActivityOrderSummaryBinding;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.utils.constants.IntentConstants;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderSummaryActivity extends AppCompatActivity {

    ActivityOrderSummaryBinding binding;
    OrderSummaryRVAdapter adapter;
    OrderSummaryViewModel viewModel;
    String TAG = "checkout";
    Long payable;

    String orderId = UUID.randomUUID().toString();
    ArrayList<CartItem> allCartItems = new ArrayList<>();
    ArrayList<ProductItem> allProductItems = new ArrayList<>();

    // User Details
    String fullName, address, phoneNo, addressType;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        binding = ActivityOrderSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the intent values of address
        Intent intent = getIntent();
        fullName = intent.getStringExtra(IntentConstants.DELIVERY_NAME);
        address = intent.getStringExtra(IntentConstants.DELIVERY_ADDRESS);
        phoneNo = intent.getStringExtra(IntentConstants.DELIVERY_PHONE);
        addressType = intent.getStringExtra(IntentConstants.ADDRESS_TYPE);
        payable = intent.getLongExtra(IntentConstants.PAY_AMOUNT, 0L);

        binding.deliverToName.setText(fullName);
        binding.deliveryAddress.setText(address);
        binding.deliverToPhoneNo.setText(phoneNo);
        binding.deliverToAddressType.setText(addressType);


        // Initialize the viewModel
        viewModel = new ViewModelProvider(this).get(OrderSummaryViewModel.class);

        // Initialize the RecyclerView
        adapter = new OrderSummaryRVAdapter(this);
        binding.orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderSummaryRecyclerView.setAdapter(this.adapter);

        // On payment Button Clicked
        binding.proceedToPaymentBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(OrderSummaryActivity.this, PaymentMethodsActivity.class);
            startActivity(intent1);
        });

        // Change address button clicked
        binding.changeAddressBtn.setOnClickListener(view -> finish());


        // Get summary details
        LiveData[] list = viewModel.getAllOrderSummaryItems(FirebaseAuth.getInstance().getUid());

        LiveData<List<CartItem>> cartItems = (LiveData<List<CartItem>>) Array.get(list, 0);
        LiveData<List<ProductItem>> productItems = (LiveData<List<ProductItem>>) Array.get(list, 1);


        cartItems.observe(this, cartItems1 -> {
            adapter.updateAllCartItems(cartItems1);
            allCartItems.clear();
            allCartItems.addAll(cartItems1);
        });

        productItems.observe(this, productItems1 -> {

            adapter.updateAllCartProductItems(productItems1);
            allProductItems.clear();
            allProductItems.addAll(productItems1);

            if (productItems1.size() == 0) {
                binding.priceDetails.setVisibility(View.GONE);
            } else {
                binding.priceDetails.setVisibility(View.VISIBLE);


                long actualAmount = 0L;
                long totalAmount = 0L;
                long discountPrice = 0L;
                long deliveryCharge = 0L;
                long deliveryChargeWaver = 0L;

                for (int i = 0; i < productItems1.size(); i++) {
                    actualAmount = Long.parseLong(productItems1.get(i).getActualPrice()) * allCartItems.get(i).getQuantity() + actualAmount;
                    totalAmount = Long.parseLong(productItems1.get(i).getListedPrice()) * allCartItems.get(i).getQuantity() + totalAmount;
                    discountPrice = totalAmount - actualAmount;

                    deliveryCharge = Long.parseLong(productItems1.get(i).getDeliveryCharge()) + deliveryCharge;
                }
                binding.priceTotal.setText(String.valueOf("₹" + totalAmount));
                binding.discountPrice.setText(String.valueOf("- ₹" + discountPrice));
                binding.deliverPrice.setText(String.valueOf("₹" + deliveryCharge));
                if (actualAmount >= 500) {
                    deliveryChargeWaver = deliveryCharge;
                } else {
                    deliveryChargeWaver = 0L;
                }

                if (deliveryChargeWaver > 0L) {
                    binding.deliverPriceWaverLayout.setVisibility(View.VISIBLE);
                    binding.deliverPriceWaver.setText(String.valueOf("- ₹" + deliveryChargeWaver));
                } else {
                    binding.deliverPriceWaverLayout.setVisibility(View.GONE);
                }

                binding.totalAmount.setText("₹" + (actualAmount + deliveryCharge - deliveryChargeWaver));
                binding.savePrice.setText("₹" + (discountPrice + deliveryChargeWaver));

                adapter.updatePayable(actualAmount + deliveryCharge - deliveryChargeWaver);
            }

        });
    }

}