package com.example.plantonic.ui.search;

import static com.example.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantonic.Adapter.listeners.OnSearchListener;
import com.example.plantonic.Adapter.SearchResultAdapter;
import com.example.plantonic.R;
import com.example.plantonic.databinding.FragmentSearchBinding;
import com.example.plantonic.firebaseClasses.search.SearchProductItem;
import com.example.plantonic.ui.productDetailsScreen.ProductViewFragment;
import com.example.plantonic.utils.HomeUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment implements OnSearchListener {

    FragmentSearchBinding binding;
    SearchResultAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);

        // Initialize recycler view
        adapter = new SearchResultAdapter(this.requireContext(), this);
        binding.searchResultRecyclerView.setLayoutManager(new GridLayoutManager(this.requireContext(), 2));
        binding.searchResultRecyclerView.setAdapter(this.adapter);

        // Handle back btn
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });




        // On Search
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("Search", newText);

                FirebaseFirestore.getInstance().collection("searchIndex").whereArrayContains("search_keyword", newText)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<SearchProductItem> searchList = queryDocumentSnapshots.toObjects(SearchProductItem.class);
                                adapter.updateSearchList(searchList);

                                if (queryDocumentSnapshots.isEmpty() && !newText.equals("")){
                                    binding.noResultFoundLabel.setVisibility(View.VISIBLE);
                                }else {
                                    binding.noResultFoundLabel.setVisibility(View.GONE);
                                }
                            }
                        });
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void OnSearchProductClicked(SearchProductItem item) {
        ProductViewFragment productViewFragment = new ProductViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, item.getProductId());
        productViewFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction
                .setReorderingAllowed(true)
                .addToBackStack("detailsScreen")
                .replace(R.id.fragmentContainerView, productViewFragment);
        fragmentTransaction.commit();
    }


    //backspaced backstack
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager manager = getActivity().getSupportFragmentManager();


                if (Objects.equals(HomeUtil.lastFragment, "")){
                    manager.popBackStackImmediate();
                }

                manager.popBackStackImmediate();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}