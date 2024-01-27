package com.example.plantonic.ui.cartfav;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantonic.Adapter.FavouriteRecyclerViewAdapter;
import com.example.plantonic.Adapter.listeners.FavouriteListener;
import com.example.plantonic.R;

import com.example.plantonic.databinding.FragmentFavouriteBinding;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.activity.home.HomeActivity;
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment;
import com.example.plantonic.utils.CartUtil;
import com.example.plantonic.utils.FavUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;


public class FavouriteFragment extends Fragment implements FavouriteListener {
    FragmentFavouriteBinding binding;
    FavouriteViewModel viewModel;

    private FavouriteRecyclerViewAdapter favouriteRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(getLayoutInflater());


        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        //favouriteRecyclerView adapter
        favouriteRecyclerViewAdapter = new FavouriteRecyclerViewAdapter(this.getContext(), this, viewModel);
        binding.favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        binding.favouriteRecyclerView.setAdapter(this.favouriteRecyclerViewAdapter);

        binding.noFavView.setVisibility(View.GONE);
        binding.favProgressBar.setVisibility(View.VISIBLE);


        // Get all fav Items
        viewModel.getAllFavItems(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> favouriteItems) {
                favouriteRecyclerViewAdapter.updateAllFavItems(favouriteItems);

                binding.favProgressBar.setVisibility(View.GONE);

                if (favouriteItems.size()==0){
                    binding.noFavView.setVisibility(View.VISIBLE);
                }
                else {
                    binding.noFavView.setVisibility(View.GONE);
                }
            }
        });

        binding.favBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onGoToCartBtnClicked(String productId) {

        FragmentManager manager = requireActivity().getSupportFragmentManager();

//        if (Objects.equals(FavUtil.lastFragment, "cart")){
//            manager.popBackStackImmediate();
//            FavUtil.lastFragment = "";
//        }

        CartUtil.lastFragment = "fav";
        Navigation.findNavController(binding.getRoot()).navigate(R.id.cartFragment,null, new NavOptions.Builder().setPopUpTo(R.id.favouriteFragment, true).build());

    }


    @Override
    public void onProductClicked(ProductItem productItem) {
        ProductViewFragment productViewFragment = new ProductViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, productItem.productId);
        productViewFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction
                .setReorderingAllowed(true)
                .addToBackStack("detailsScreenFromFav")
                .replace(R.id.fragmentContainerView, productViewFragment);
        fragmentTransaction.commit();

        FavUtil.lastFragment = "product";
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Objects.equals(FavUtil.lastFragment, "cart")){
            binding.favBackBtn.setVisibility(View.VISIBLE);
            ((HomeActivity)requireActivity()).hideBottomNavBar();
        }else{
            binding.favBackBtn.setVisibility(View.GONE);
        }
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

    //backspaced backstack
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                FragmentManager manager = getActivity().getSupportFragmentManager();

                if (Objects.equals(FavUtil.lastFragment, "cart") || Objects.equals(CartUtil.lastFragment, "fav")){
                    manager.popBackStackImmediate();
                }
                while(manager.getBackStackEntryCount() > 1 && !Objects.equals(FavUtil.lastFragment, "")){
                    manager.popBackStackImmediate();
                    FavUtil.lastFragment = "";
                }


                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                navController.navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.favouriteFragment, true).build());
                manager.popBackStackImmediate();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }


}