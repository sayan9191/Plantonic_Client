package com.example.plantonic.ui.cartfav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


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
}