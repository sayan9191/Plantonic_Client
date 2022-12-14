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

import com.example.plantonic.Adapter.OrderSummaryRVAdapter;
import com.example.plantonic.R;
import com.example.plantonic.databinding.ActivityOrderSummaryBinding;
import com.example.plantonic.firebaseClasses.CartItem;
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

public class OrderSummaryActivity extends AppCompatActivity  implements PaymentResultListener {

    ActivityOrderSummaryBinding binding;
    OrderSummaryRVAdapter adapter;
    OrderSummaryViewModel viewModel;
    String TAG= "checkout";
    Long payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        binding = ActivityOrderSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the intent values of address
        Intent intent = getIntent();
        binding.deliverToName.setText(intent.getStringExtra(IntentConstants.DELIVERY_NAME));
        binding.deliveryAddress.setText(intent.getStringExtra(IntentConstants.DELIVERY_ADDRESS));
        binding.deliverToPhoneNo.setText(intent.getStringExtra(IntentConstants.DELIVERY_PHONE));
        binding.deliverToAddressType.setText(intent.getStringExtra(IntentConstants.ADDRESS_TYPE));
        payable = intent.getLongExtra(IntentConstants.PAY_AMOUNT,0L);

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


        // Get summary details
        LiveData[] list = viewModel.getAllOrderSummaryItems(FirebaseAuth.getInstance().getUid());

        LiveData<List<CartItem>> cartItems = (LiveData<List<CartItem>>) Array.get(list, 0);
        LiveData<List<ProductItem>> productItems = (LiveData<List<ProductItem>>) Array.get(list, 1);


        ArrayList<CartItem> allCartItems = new ArrayList<>();

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

                if (productItems.size() == 0) {
                    binding.priceDetails.setVisibility(View.GONE);
                } else {
                    binding.priceDetails.setVisibility(View.VISIBLE);


                    Long actualAmount = 0L;
                    Long totalAmount = 0L;
                    Long discountPrice = 0L;
                    for (int i = 0; i < productItems.size(); i++) {
                        actualAmount = Long.parseLong(productItems.get(i).getActualPrice()) * allCartItems.get(i).getQuantity() + actualAmount;
                        totalAmount = Long.parseLong(productItems.get(i).getListedPrice()) * allCartItems.get(i).getQuantity() + totalAmount;
                        discountPrice = totalAmount - actualAmount;
                    }
                    binding.priceTotal.setText(String.valueOf("₹" + totalAmount + "/-"));
                    binding.discountPrice.setText(String.valueOf("- ₹" + discountPrice + "/-"));
                    binding.deliverPrice.setText("₹" + 50 + "/-");
                    binding.totalAmount.setText(String.valueOf("₹" + (actualAmount + 50) + "/-"));
//                    binding.placeOrderTotalAmount.setText("₹" + totalAmount + "/-");
//                    binding.placeOrderPayAmount.setText(String.valueOf("₹" + (actualAmount + 50) + "/-"));
                    binding.savePrice.setText(String.valueOf("₹" + discountPrice + "/-"));

//                    payablePrice = actualAmount + 50;
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
        binding.summaryProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onPaymentSuccess: "+s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        binding.summaryProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onPaymentError: "+s);
    }
}