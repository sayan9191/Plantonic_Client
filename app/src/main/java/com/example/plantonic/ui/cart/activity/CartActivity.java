package com.example.plantonic.ui.cart.activity;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.plantonic.Adapter.CartRecyclerViewAdapter;
import com.example.plantonic.Adapter.listeners.CartListner;
import com.example.plantonic.CheckOutOne;
import com.example.plantonic.R;
import com.example.plantonic.databinding.ActivityCartBinding;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.cart.CartViewModel;
import com.example.plantonic.ui.cart.activity.ActivityCartViewModel;
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartListner {


    ActivityCartBinding binding;
    ActivityCartViewModel cartViewModel;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartViewModel = new ViewModelProvider(this).get(ActivityCartViewModel.class);

        // CartRecyclerViewAdapter
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(this, this, cartViewModel);
        binding.activityCartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.activityCartRecyclerView.setAdapter(this.cartRecyclerViewAdapter);

        LiveData[] list = cartViewModel.getAllCartItems(FirebaseAuth.getInstance().getUid());

        LiveData<List<CartItem>> cartItems = (LiveData<List<CartItem>>) Array.get(list, 0);
        LiveData<List<ProductItem>> productItems = (LiveData<List<ProductItem>>) Array.get(list, 1);


        ArrayList<CartItem> allCartItems = new ArrayList<>();

        cartItems.observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartRecyclerViewAdapter.updateAllCartItems(cartItems);
                allCartItems.clear();
                allCartItems.addAll(cartItems);
            }
        });

        productItems.observe(this, new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {
                cartRecyclerViewAdapter.updateAllCartProductItems(productItems);

                if (productItems.size() == 0) {
                    binding.activityNoCartView.setVisibility(View.VISIBLE);
                    binding.activityPlaceOrderLabel.setVisibility(View.GONE);
                    binding.activityPriceDetails.setVisibility(View.GONE);
                } else {
                    binding.activityNoCartView.setVisibility(View.GONE);
                    binding.activityPlaceOrderLabel.setVisibility(View.VISIBLE);
                    binding.activityPriceDetails.setVisibility(View.VISIBLE);


                    Long totalPrice = 0L;
                    Long actualPrice = 0L;
                    Long discountPrice = 0L;
                    for (int i = 0; i < productItems.size(); i++) {
                        totalPrice = Long.parseLong(productItems.get(i).getActualPrice()) * allCartItems.get(i).getQuantity() + totalPrice;
                        actualPrice = Long.parseLong(productItems.get(i).getListedPrice()) * allCartItems.get(i).getQuantity() + actualPrice;
                        discountPrice = actualPrice - totalPrice;
                    }
                    binding.activityPriceTotal.setText(String.valueOf("₹" + actualPrice + "/-"));
                    binding.activityDiscountPrice.setText(String.valueOf("₹" + discountPrice + "/-"));
                    binding.activityDeliverPrice.setText("₹" + 50 + "/-");
                    binding.activityTotalAmount.setText(String.valueOf("₹" + (totalPrice + 50) + "/-"));
                    binding.activityPlaceOrderTotalAmount.setText("₹" + actualPrice + "/-");
                    binding.activityPlaceOrderPayAmount.setText(String.valueOf("₹" + (totalPrice + 50) + "/-"));
                    binding.activitySavePrice.setText(String.valueOf("₹" + discountPrice + "/-"));
                }
            }
        });
        binding.activityPlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true)
                        .addToBackStack("checkout1")
                        .replace(R.id.fragmentContainerView, new CheckOutOne());
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void onRemoveFromCartClicked(ProductItem productItem) {
        cartViewModel.removeFromCart(FirebaseAuth.getInstance().getUid(), productItem.getProductId());
    }

    @Override
    public void onCartItemClicked(ProductItem productItem) {
        ProductViewFragment productViewFragment = new ProductViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, productItem.productId);
        productViewFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .setReorderingAllowed(true)
                .addToBackStack("detailsScreenFromCart")
                .replace(R.id.fragmentContainerView, productViewFragment);
        fragmentTransaction.commit();
    }
}