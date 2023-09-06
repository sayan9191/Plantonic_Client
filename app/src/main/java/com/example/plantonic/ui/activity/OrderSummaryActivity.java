package com.example.plantonic.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.plantonic.Adapter.OrderSummaryRVAdapter;
import com.example.plantonic.R;
import com.example.plantonic.databinding.ActivityOrderSummaryBinding;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.OrderItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.utils.constants.IntentConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderSummaryActivity extends AppCompatActivity  implements PaymentResultListener {

    ActivityOrderSummaryBinding binding;
    OrderSummaryRVAdapter adapter;
    OrderSummaryViewModel viewModel;
    String TAG= "checkout";
    Long payable;

    String orderId = UUID.randomUUID().toString();
    ArrayList<CartItem> allCartItems = new ArrayList<>();
    ArrayList<ProductItem> allProductItems = new ArrayList<>();

    // User Details
    String fullName, address, phoneNo, addressType;

    // Last order
    String lastOrder = "";

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
        payable = intent.getLongExtra(IntentConstants.PAY_AMOUNT,0L);

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
        binding.proceedToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(payable);
                binding.summaryProgressBar.setVisibility(View.VISIBLE);
            }
        });

        // Change address button clicked
        binding.changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Get summary details
        LiveData[] list = viewModel.getAllOrderSummaryItems(FirebaseAuth.getInstance().getUid());

        LiveData<List<CartItem>> cartItems = (LiveData<List<CartItem>>) Array.get(list, 0);
        LiveData<List<ProductItem>> productItems = (LiveData<List<ProductItem>>) Array.get(list, 1);



        cartItems.observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                adapter.updateAllCartItems(cartItems);
                allCartItems.clear();
                allCartItems.addAll(cartItems);
            }
        });

        productItems.observe(this, new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {

                adapter.updateAllCartProductItems(productItems);
                allProductItems.clear();
                allProductItems.addAll(productItems);

                if (productItems.size() == 0) {
                    binding.priceDetails.setVisibility(View.GONE);
                } else {
                    binding.priceDetails.setVisibility(View.VISIBLE);


                    Long actualAmount = 0L;
                    Long totalAmount = 0L;
                    Long discountPrice = 0L;
                    Long deliveryCharge = 0L;
                    Long deliveryChargeWaver = 0L;

                    for (int i = 0; i < productItems.size(); i++) {
                        actualAmount = Long.parseLong(productItems.get(i).getActualPrice()) * allCartItems.get(i).getQuantity() + actualAmount;
                        totalAmount = Long.parseLong(productItems.get(i).getListedPrice()) * allCartItems.get(i).getQuantity() + totalAmount;
                        discountPrice = totalAmount - actualAmount;

                        deliveryCharge = Long.parseLong(productItems.get(i).getDeliveryCharge()) + deliveryCharge;
                    }
                    binding.priceTotal.setText(String.valueOf("₹" + totalAmount));
                    binding.discountPrice.setText(String.valueOf("- ₹" + discountPrice));
                    binding.deliverPrice.setText(String.valueOf("₹" + deliveryCharge));
                    if (actualAmount >= 500){
                        deliveryChargeWaver = deliveryCharge;
                    }else {
                        deliveryChargeWaver = 0L;
                    }

                    if (deliveryChargeWaver > 0L){
                        binding.deliverPriceWaverLayout.setVisibility(View.VISIBLE);
                        binding.deliverPriceWaver.setText(String.valueOf("- ₹" + deliveryChargeWaver ));
                    }else{
                        binding.deliverPriceWaverLayout.setVisibility(View.GONE);
                    }

                    binding.totalAmount.setText(String.valueOf("₹" + (actualAmount + deliveryCharge - deliveryChargeWaver)));
                    binding.savePrice.setText(String.valueOf("₹" + (discountPrice + deliveryChargeWaver)));

                    adapter.updatePayable(actualAmount + deliveryCharge - deliveryChargeWaver);
                }

            }
        });
    }


    private void startPayment(Long price) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_krjMSuWqbBTfqs");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.icon);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Plantonic");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#37AC6C");
            options.put("currency", "INR");
            options.put("amount", price*100);//pass amount in currency subunits
//            options.put("prefill.email", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
            options.put("prefill.contact", binding.deliverToPhoneNo.getText());
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);
            binding.summaryProgressBar.setVisibility(View.GONE);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
            binding.summaryProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {

        Log.d(TAG, "onPaymentSuccess: "+s);

        for (int i = 0; i < allProductItems.size(); i++){



            CartItem currentCartItem = allCartItems.get(i);
            ProductItem currentProductItem = allProductItems.get(i);

            this.lastOrder = currentProductItem.getProductId();

            Long deliveryCharge;

            if (payable >= 500){
                deliveryCharge = 0L;
            }else{
                deliveryCharge = Long.parseLong(currentProductItem.getDeliveryCharge());
            }

            orderId = UUID.randomUUID().toString();

            viewModel.placeOrder(new OrderItem(orderId, currentProductItem.getMerchantId(),
                    currentProductItem.getProductId(),
                    currentCartItem.getUserId(),
                    fullName, address, addressType, phoneNo,
                    "online/razorpay",
                    currentCartItem.getQuantity(), "order placed",
                    s, System.currentTimeMillis(),
                    String.valueOf(Long.parseLong(currentProductItem.getActualPrice()) * currentCartItem.getQuantity()),
                    String.valueOf(Long.parseLong(currentProductItem.getListedPrice()) * currentCartItem.getQuantity()),
                    String.valueOf(deliveryCharge), -1L));
        }

        viewModel.getLastPlacedOrderId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String placedOrderId) {
                if (Objects.equals(OrderSummaryActivity.this.lastOrder, placedOrderId)){
                    binding.summaryProgressBar.setVisibility(View.GONE);
                    startActivity(new Intent(OrderSummaryActivity.this, ThankYouOrderActivity.class));
                }
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        binding.summaryProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onPaymentError: "+s);
        Toast.makeText(this, "Payment failed. Try again", Toast.LENGTH_SHORT).show();
    }
}