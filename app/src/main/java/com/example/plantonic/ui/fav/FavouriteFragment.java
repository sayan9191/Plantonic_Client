package com.example.plantonic.ui.fav;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plantonic.Adapter.FavouriteRecyclerViewAdapter;
import com.example.plantonic.Adapter.listeners.FavouriteListener;
import com.example.plantonic.R;

import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment;
import com.example.plantonic.utils.CartUtil;
import com.example.plantonic.utils.FavUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;


public class FavouriteFragment extends Fragment implements FavouriteListener {
    RecyclerView favouriteRecyclerView;
    TextView noFavView;
    View view;
    FavouriteViewModel viewModel;

    private FavouriteRecyclerViewAdapter favouriteRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        noFavView = view.findViewById(R.id.noFavView);
        favouriteRecyclerView = view.findViewById(R.id.favouriteRecyclerView);

        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        //favouriteRecyclerView adapter
        favouriteRecyclerViewAdapter = new FavouriteRecyclerViewAdapter(this.getContext(), this, viewModel);
        favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        favouriteRecyclerView.setAdapter(this.favouriteRecyclerViewAdapter);


        // Get all fav Items
        viewModel.getAllFavItems(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> favouriteItems) {
                favouriteRecyclerViewAdapter.updateAllFavItems(favouriteItems);

                if (favouriteItems.size()==0){
                    noFavView.setVisibility(View.VISIBLE);
                }
                else {
                    noFavView.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }

    @Override
    public void onGoToCartBtnClicked(String productId) {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        if (Objects.equals(CartUtil.lastFragment, "home")){
            CartUtil.homeStackCount +=1;
        }
        CartUtil.homeStackCount +=1;
        CartUtil.lastFragment = "fav";
        Navigation.findNavController(view).navigate(R.id.cartFragment,null, new NavOptions.Builder().setPopUpTo(R.id.favouriteFragment, true).build());

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


    //backspaced backstack
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                FragmentManager manager = getActivity().getSupportFragmentManager();

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