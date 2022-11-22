package com.example.plantonic.ui.productDetailsScreen;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.plantonic.R;
import com.example.plantonic.ui.firebaseClasses.ProductItem;
import com.example.plantonic.ui.homeFragment.HomeFragment;

import java.util.ArrayList;
import java.util.Objects;


public class ProductViewFragment extends Fragment {
    com.denzcoskun.imageslider.ImageSlider imageSlider;
    TextView name, productPrice, productActualPrice, productDescription,productDetails,productDiscount;
    ImageView backBtn;
    com.google.android.material.floatingactionbutton.FloatingActionButton shareBtn;
    NestedScrollView productDetailsScrollView;
    ProgressBar progressBar;
    int productNo = 0;
    TextView integer_number;
    Button decrease,increase;
    View view;
    String productId;
    private ProductViewModel productViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_view, container, false);
        imageSlider = view.findViewById(R.id.productImages);
        backBtn = view.findViewById(R.id.backBtn);
        name = view.findViewById(R.id.productName);
        shareBtn = view.findViewById(R.id.shareBtn);
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

        try{
            productId = getArguments().getString(PRODUCT_ID);
            progressBar.setVisibility(View.VISIBLE);
            productDetailsScrollView.setVisibility(View.GONE);
        }catch (Exception e){
            productId = null;
            getParentFragmentManager().popBackStackImmediate();
        }

        if (productId!= null) {
            productViewModel.getProductDetailsFromId(productId).observe(getViewLifecycleOwner(), new Observer< ProductItem >() {

                @Override
                public void onChanged(ProductItem productItem) {
                    if (productItem!= null && Objects.equals(productItem.productId, productId)){
                        productDetailsScrollView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        productDetails.setText(productItem.productName);
                        name.setText(productItem.productName);
                        productActualPrice.setText("₹ "+ productItem.listedPrice+ "/-");
                        productPrice.setText("₹ "+ productItem.actualPrice+ "/-");
                        int realPrice= Integer.parseInt(productItem.listedPrice);
                        int price = Integer.parseInt(productItem.actualPrice);
                        int discount = (realPrice - price)*100/realPrice;
                        productDiscount.setText(discount +"% off");
                        productDescription.setText(productItem.productDescription);

                        shareBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(Intent.ACTION_SEND);
                                myIntent.setType("text/plain");
                                String body ="This is your product " + productItem.productName + productItem.actualPrice + " Launching discount"+ discount;
                                String sub = "Your Subject";
                                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                                startActivity(Intent.createChooser(myIntent, "Share Using"));
                            }
                        });

                        ArrayList<SlideModel> slideModels = new ArrayList<>();

                        if (!Objects.equals(productItem.imageUrl1, "")){
                            slideModels.add(new SlideModel(productItem.imageUrl1, ScaleTypes.CENTER_CROP));
                        }
                        if (!Objects.equals(productItem.imageUrl2, "")){
                            slideModels.add(new SlideModel(productItem.imageUrl2, ScaleTypes.CENTER_CROP));
                        }
                        if (!Objects.equals(productItem.imageUrl3, "")){
                            slideModels.add(new SlideModel(productItem.imageUrl3, ScaleTypes.CENTER_CROP));
                        }
                        if (!Objects.equals(productItem.imageUrl4, "")) {
                            slideModels.add(new SlideModel(productItem.imageUrl4, ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                    }else{
                        productDetailsScrollView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            });

        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction
                        .setReorderingAllowed(true).addToBackStack("back").replace(R.id.fragmentContainerView, new HomeFragment());
                fragmentTransaction.commit();
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseInteger(view);
            }
        });
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
        if (productNo>0){
            display(productNo);
        }
        else{
            productNo= 0;
            display((0));
        }
    }
    private void display(int number) {
        integer_number.setText("" + number);
    }
}