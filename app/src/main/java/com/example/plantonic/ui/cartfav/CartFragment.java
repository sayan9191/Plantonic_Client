package com.example.plantonic.ui.cartfav;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantonic.Adapter.CartRecyclerViewAdapter;
import com.example.plantonic.Adapter.listeners.CartListner;

import com.example.plantonic.databinding.FragmentCartBinding;
import com.example.plantonic.ui.activity.CheckoutActivity;
import com.example.plantonic.ui.activity.home.HomeActivity;
import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment;
import com.example.plantonic.utils.CartUtil;
import com.example.plantonic.utils.FavUtil;
import com.example.plantonic.utils.ProductUtil;
import com.example.plantonic.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements CartListner {


    FragmentCartBinding binding;
    CartViewModel cartViewModel;
    Long payablePrice= 0L;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(getLayoutInflater(), container, false);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // CartRecyclerViewAdapter
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(this.getContext(), this, cartViewModel);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        binding.cartRecyclerView.setAdapter(this.cartRecyclerViewAdapter);

        LiveData[] list = cartViewModel.getAllCartItems(FirebaseAuth.getInstance().getUid());

        LiveData<List<CartItem>> cartItems = (LiveData<List<CartItem>>) Array.get(list, 0);
        LiveData<List<ProductItem>> productItems = (LiveData<List<ProductItem>>) Array.get(list, 1);


        ArrayList<CartItem> allCartItems = new ArrayList<>();

        cartItems.observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {

                binding.cartProgressBar.setVisibility(View.GONE);

                if (cartItems.size() == 0) {
                    binding.noCartView.setVisibility(View.VISIBLE);
                    binding.placeOrderLabel.setVisibility(View.GONE);
                    binding.priceDetails.setVisibility(View.GONE);
                } else {
                    binding.noCartView.setVisibility(View.GONE);
                    binding.placeOrderLabel.setVisibility(View.VISIBLE);
                    binding.priceDetails.setVisibility(View.VISIBLE);
                }

                cartRecyclerViewAdapter.updateAllCartItems(cartItems);
                allCartItems.clear();
                allCartItems.addAll(cartItems);
            }
        });

        productItems.observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {

                cartRecyclerViewAdapter.updateAllCartProductItems(productItems);

                if (productItems.size() > 0) {

                    Long actualAmount = 0L;
                    Long totalAmount = 0L;
                    Long discountPrice = 0L;
                    Long deliveryCharge = 0L;
                    Long deliveryChargeWaver = 0L;

                    for (int i = 0; i < productItems.size(); i++) {
                        actualAmount = Long.parseLong(productItems.get(i).getActualPrice()) * allCartItems.get(i).getQuantity() + actualAmount;
                        totalAmount = Long.parseLong(productItems.get(i).getListedPrice()) * allCartItems.get(i).getQuantity() + totalAmount;
                        deliveryCharge = Long.parseLong(productItems.get(i).getDeliveryCharge()) * allCartItems.get(i).getQuantity() + deliveryCharge;
                    }

                    discountPrice = totalAmount - actualAmount;

                    if (actualAmount >= 500){
                        deliveryChargeWaver = deliveryCharge;
                    }else {
                        deliveryChargeWaver = 0L;
                    }

                    binding.priceTotal.setText(String.valueOf("₹" + StringUtil.Companion.getFormattedPrice(totalAmount)));
                    binding.discountPrice.setText(String.valueOf("- ₹" + StringUtil.Companion.getFormattedPrice(discountPrice) ));
                    binding.deliverPrice.setText(String.valueOf("₹" + StringUtil.Companion.getFormattedPrice(deliveryCharge) ));

                    if (deliveryChargeWaver > 0L){
                        binding.deliverPriceWaverLayout.setVisibility(View.VISIBLE);
                        binding.deliverPriceWaver.setText(String.valueOf("- ₹" + StringUtil.Companion.getFormattedPrice(deliveryChargeWaver) ));
                    }else{
                        binding.deliverPriceWaverLayout.setVisibility(View.GONE);
                    }

                    binding.totalAmount.setText(String.valueOf("₹" + StringUtil.Companion.getFormattedPrice(actualAmount + deliveryCharge - deliveryChargeWaver)));
                    binding.placeOrderTotalAmount.setText("₹" + StringUtil.Companion.getFormattedPrice(totalAmount + deliveryCharge));
                    binding.placeOrderPayAmount.setText(String.valueOf("₹" + StringUtil.Companion.getFormattedPrice(actualAmount + deliveryCharge - deliveryChargeWaver)));
                    binding.savePrice.setText(String.valueOf("₹" + StringUtil.Companion.getFormattedPrice(discountPrice + deliveryChargeWaver)));

                    payablePrice = actualAmount + deliveryCharge - deliveryChargeWaver;
                    cartRecyclerViewAdapter.updateTotalPayable(actualAmount);
                }
            }
        });
        binding.placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                // trying to set total price in razorpay
                intent.putExtra("payablePrice", payablePrice);
                startActivity(intent);
            }
        });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requireActivity().onBackPressed();
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
        });

        return binding.getRoot();
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

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction
                .setReorderingAllowed(true)
                .addToBackStack("detailsScreenFromCart")
                .replace(R.id.fragmentContainerView, productViewFragment);
        fragmentTransaction.commit();

        if (Objects.equals(CartUtil.lastFragment, "")){
            ProductUtil.lastFragment = "cart";
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Objects.equals(CartUtil.lastFragment, "home") || Objects.equals(CartUtil.lastFragment, "product") || (Objects.equals(FavUtil.lastFragment, "cart") && Objects.equals(CartUtil.lastFragment, "fav"))){
            ((HomeActivity)requireActivity()).hideBottomNavBar();
            binding.backBtn.setVisibility(View.VISIBLE);
        }else{
            binding.backBtn.setVisibility(View.GONE);
        }
    }


    //backspaced backstack
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                try {

                    String lastFragment = CartUtil.lastFragment;

                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    if (manager.getBackStackEntryCount() > 1 && !Objects.equals(CartUtil.lastFragment, "")) {
                        manager.popBackStackImmediate();
                        CartUtil.lastFragment = "";
                    }

                    NavController navController = Navigation.findNavController(binding.getRoot());
                    if (Objects.equals(lastFragment, "product") || Objects.equals(FavUtil.lastFragment, "cart")){
                        navController.navigate(R.id.favouriteFragment, null, new NavOptions.Builder().setPopUpTo(R.id.cartFragment, true).build());
                        CartUtil.lastFragment = "";
                    }else if (!Objects.equals(ProductUtil.lastFragment, "orders")){
                        navController.navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.cartFragment, true).build());
                    }

                    manager.popBackStackImmediate();
                    ((HomeActivity)requireActivity()).showBottomNavBar();



//                    }
                } catch (Exception e) {
                    e.getStackTrace();

                }


            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

    }
}

