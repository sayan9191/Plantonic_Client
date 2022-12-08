package com.example.plantonic.ui.productDetailsScreen;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.FavouriteItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.cart.activity.CartActivity;
import com.example.plantonic.ui.homeFragment.HomeFragment;
import com.example.plantonic.utils.CartUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;


public class ProductViewFragment extends Fragment {
    com.denzcoskun.imageslider.ImageSlider imageSlider;
    TextView addToCartBtn;
    TextView name, productPrice, productActualPrice, productDescription, productDetails, productDiscount;
    ImageView backBtn;
    com.google.android.material.floatingactionbutton.FloatingActionButton shareBtn, favouriteBtn;
    NestedScrollView productDetailsScrollView;
    ProgressBar progressBar;
    int productNo = 1;
    TextView integer_number;
    TextView decrease, increase;
    View view;


    String productId;
    private ProductViewModel productViewModel;
    private boolean isFavourite = false;
    private boolean isCart = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_view, container, false);
        imageSlider = view.findViewById(R.id.productImages);
        backBtn = view.findViewById(R.id.backBtn);
        addToCartBtn = view.findViewById(R.id.addToCartBtn);
        name = view.findViewById(R.id.productName);
        shareBtn = view.findViewById(R.id.shareBtn);
        favouriteBtn = view.findViewById(R.id.favouriteBtn);
        productPrice = view.findViewById(R.id.productPrice);
        productActualPrice = view.findViewById(R.id.realPrice);
        productDetails = view.findViewById(R.id.productDetails);
        productDescription = view.findViewById(R.id.productDescription);
        productDiscount = view.findViewById(R.id.discount);
        integer_number = view.findViewById(R.id.integer_number);
        decrease = view.findViewById(R.id.decrease);
        increase = view.findViewById(R.id.increase);
        productDetailsScrollView = view.findViewById(R.id.productDetailsScrollView);
        progressBar = view.findViewById(R.id.productDetailsProgressBar);



        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        try {
            productId = getArguments().getString(PRODUCT_ID);
            progressBar.setVisibility(View.VISIBLE);
            productDetailsScrollView.setVisibility(View.GONE);
        } catch (Exception e) {
            productId = null;
            getParentFragmentManager().popBackStackImmediate();
        }


        if (productId != null) {

            /*
              Getting all data about product
             */
            productViewModel.getProductDetailsFromId(productId).observe(getViewLifecycleOwner(), new Observer<ProductItem>() {

                @Override
                public void onChanged(ProductItem productItem) {
                    if (productItem != null && Objects.equals(productItem.productId, productId)) {
                        productDetailsScrollView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        productDetails.setText(productItem.productName);
                        name.setText(productItem.productName);
                        productActualPrice.setText("₹ " + productItem.listedPrice + "/-");
                        productPrice.setText("₹ " + productItem.actualPrice + "/-");
                        int realPrice = Integer.parseInt(productItem.listedPrice);
                        int price = Integer.parseInt(productItem.actualPrice);
                        int discount = (realPrice - price) * 100 / realPrice;
                        productDiscount.setText(discount + "% off");
                        productDescription.setText(productItem.productDescription);

                        //share button of Product
                        shareBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(Intent.ACTION_SEND);
                                myIntent.setType("text/plain");
                                String body = "This is your product " + productItem.productName + productItem.actualPrice + " Launching discount" + discount;
                                String sub = "Your Subject";
                                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                                myIntent.putExtra(Intent.EXTRA_TEXT, body);
                                startActivity(Intent.createChooser(myIntent, "Share Using"));
                            }
                        });


                        // favourite Button of Product

                        favouriteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!isFavourite) {
                                    productViewModel.addToFav(new FavouriteItem(FirebaseAuth.getInstance().getUid()
                                            , productItem.productId, productItem.merchantId, System.currentTimeMillis()));
                                } else {
                                    productViewModel.removeFromFav(FirebaseAuth.getInstance().getUid(), productItem.productId);
                                }
                            }
                        });

                        ArrayList<SlideModel> slideModels = new ArrayList<>();

                        if (!Objects.equals(productItem.imageUrl1, "")) {
                            slideModels.add(new SlideModel(productItem.imageUrl1, ScaleTypes.CENTER_CROP));
                        }
                        if (!Objects.equals(productItem.imageUrl2, "")) {
                            slideModels.add(new SlideModel(productItem.imageUrl2, ScaleTypes.CENTER_CROP));
                        }
                        if (!Objects.equals(productItem.imageUrl3, "")) {
                            slideModels.add(new SlideModel(productItem.imageUrl3, ScaleTypes.CENTER_CROP));
                        }
                        if (!Objects.equals(productItem.imageUrl4, "")) {
                            slideModels.add(new SlideModel(productItem.imageUrl4, ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                    } else {
                        productDetailsScrollView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });

            /*
            Checking if product is favourite
            */
            productViewModel.checkIfFav(FirebaseAuth.getInstance().getUid(), productId).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isFav) {
                    isFavourite = isFav;

                    if (isFav) {
                        favouriteBtn.setForegroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.holo_red_dark));

                    } else {
                        favouriteBtn.setForegroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));

                    }

                }
            });

            /*
            Checking if product is added to cart
            */
            productViewModel.checkIfAddedToCart(FirebaseAuth.getInstance().getUid(), productId).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isAdded) {
                    if (isAdded){
                        addToCartBtn.setText("GO TO CART");
                    }else {
                        addToCartBtn.setText("ADD TO CART");
                    }
                    isCart = isAdded;
                }
            });

        }

        //Add to cart product item
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCart) {
                    productViewModel.addToCart(FirebaseAuth.getInstance().getUid(), productId, Long.parseLong(integer_number.getText().toString()));
                }else {
                    if (Objects.equals(CartUtil.lastFragment, "home")){
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        if (manager.getBackStackEntryCount() > 1)
                            manager.popBackStackImmediate();
                    }
                    CartUtil.lastFragment = "home";
                    CartUtil.homeStackCount += 1;
                    Navigation.findNavController(ProductViewFragment.this.view).navigate(R.id.cartFragment,null, new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build());

//                    startActivity(new Intent(requireContext(), CartActivity.class));
                }
            }
        });

        //back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction
                        .setReorderingAllowed(true).addToBackStack("back").replace(R.id.fragmentContainerView, new HomeFragment());
                fragmentTransaction.commit();
            }
        });

        //product Items No decrease
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseInteger(view);
            }
        });

        //product Items No. increase
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseInteger(view);
            }
        });
        return view;
    }

    public void increaseInteger(View view) {
        productNo = productNo + 1;
        display(productNo);

    }

    public void decreaseInteger(View view) {
        productNo = productNo - 1;
        if (productNo > 1) {
            display(productNo);
        } else {
            productNo = 1;
            display((1));
        }
    }

    private void display(int number) {
        integer_number.setText("" + number);
    }

    @Override
    public void onStart() {
        super.onStart();
        display(productNo);

        // Change status bar color
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.green));
    }

    @Override
    public void onStop() {
        super.onStop();
        // Change status bar color
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.white));
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (Objects.equals(CartUtil.lastFragment, "home")){
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            if (manager.getBackStackEntryCount() > 2){
//                manager.popBackStackImmediate();
//            }
//        }
//    }

    //backspaced backstack
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStackImmediate();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}