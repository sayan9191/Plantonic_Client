package com.example.plantonic.ui.cartfav;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plantonic.Adapter.CartRecyclerViewAdapter;
import com.example.plantonic.Adapter.listeners.CartListner;
import com.example.plantonic.CheckOutOne;
import com.example.plantonic.HomeActivity;
import com.example.plantonic.R;
import com.example.plantonic.databinding.FragmentCartBinding;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment;
import com.example.plantonic.utils.CartUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements CartListner {


    FragmentCartBinding binding;
    CartViewModel cartViewModel;
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
                cartRecyclerViewAdapter.updateAllCartItems(cartItems);
                allCartItems.clear();
                allCartItems.addAll(cartItems);
            }
        });

        productItems.observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {
                cartRecyclerViewAdapter.updateAllCartProductItems(productItems);

                if (productItems.size() == 0) {
                    binding.noCartView.setVisibility(View.VISIBLE);
                    binding.placeOrderLabel.setVisibility(View.GONE);
                    binding.priceDetails.setVisibility(View.GONE);
                } else {
                    binding.noCartView.setVisibility(View.GONE);
                    binding.placeOrderLabel.setVisibility(View.VISIBLE);
                    binding.priceDetails.setVisibility(View.VISIBLE);


                    Long totalPrice = 0L;
                    Long actualPrice = 0L;
                    Long discountPrice = 0L;
                    for (int i = 0; i < productItems.size(); i++) {
                        totalPrice = Long.parseLong(productItems.get(i).getActualPrice()) * allCartItems.get(i).getQuantity() + totalPrice;
                        actualPrice = Long.parseLong(productItems.get(i).getListedPrice()) * allCartItems.get(i).getQuantity() + actualPrice;
                        discountPrice = actualPrice - totalPrice;
                    }
                    binding.priceTotal.setText(String.valueOf("₹" + actualPrice + "/-"));
                    binding.discountPrice.setText(String.valueOf("₹" + discountPrice + "/-"));
                    binding.deliverPrice.setText("₹" + 50 + "/-");
                    binding.totalAmount.setText(String.valueOf("₹" + (totalPrice + 50) + "/-"));
                    binding.placeOrderTotalAmount.setText("₹" + actualPrice + "/-");
                    binding.placeOrderPayAmount.setText(String.valueOf("₹" + (totalPrice + 50) + "/-"));
                    binding.savePrice.setText(String.valueOf("₹" + discountPrice + "/-"));
                }
            }
        });
        binding.placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true)
                        .addToBackStack("checkout1")
                        .replace(R.id.fragmentContainerView, new CheckOutOne());
                fragmentTransaction.commit();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Objects.equals(CartUtil.lastFragment, "home")){
            ((HomeActivity)requireActivity()).hideBottomNavBar();
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

                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    if (manager.getBackStackEntryCount() > 1 && !Objects.equals(CartUtil.lastFragment, "")) {
                        manager.popBackStackImmediate();
                        CartUtil.lastFragment = "";
                    }
                    NavController navController = Navigation.findNavController(binding.getRoot());
                    navController.navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.cartFragment, true).build());
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
}