package co.in.plantonic.ui.search;

import static co.in.plantonic.utils.constants.IntentConstants.PRODUCT_ID;

import android.content.Context;
import android.content.pm.ActivityInfo;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import co.in.plantonic.Adapter.listeners.OnSearchListener;
import co.in.plantonic.Adapter.SearchResultAdapter;
import co.in.plantonic.R;
import co.in.plantonic.databinding.FragmentSearchBinding;
import co.in.plantonic.firebaseClasses.search.SearchProductItem;
import co.in.plantonic.ui.productDetailsScreen.ProductViewFragment;
import co.in.plantonic.utils.HomeUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment implements OnSearchListener {

    FragmentSearchBinding binding;
    SearchResultAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Initialize recycler view
        adapter = new SearchResultAdapter(this.requireContext(), this);
//        binding.searchResultRecyclerView.addItemDecoration(new DividerItemDecoration(this.requireContext(), DividerItemDecoration.HORIZONTAL));

        binding.searchResultRecyclerView.setLayoutManager(new GridLayoutManager(this.requireContext(), 2));
        binding.searchResultRecyclerView.setAdapter(this.adapter);


        // Handle back btn
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        binding.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean hasFocus) {
                if (hasFocus) {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(view.findFocus(), 0);
                        }
                    }, 200);
                }
            }
        });

        binding.searchView.requestFocus();
//        showKeyboard(requireContext(), binding.getRoot());
//        Window window = requireActivity().getWindow();
//        // Calls Soft Input Mode to make it Visible
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT);


        // On Search
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("Search", newText);
                if (!newText.isEmpty()){
                    binding.searchResultProgressBar.setVisibility(View.VISIBLE);
                    binding.noResultFoundLabel.setVisibility(View.GONE);
                }else {
                    binding.searchResultProgressBar.setVisibility(View.GONE);
                }

                FirebaseFirestore.getInstance().collection("searchIndex").whereArrayContains("search_keyword", newText)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<SearchProductItem> searchList = queryDocumentSnapshots.toObjects(SearchProductItem.class);
                                adapter.updateSearchList(searchList);

                                if (searchList.isEmpty() && !newText.equals("")){
                                    binding.noResultFoundLabel.setVisibility(View.VISIBLE);
                                    binding.searchResultProgressBar.setVisibility(View.GONE);
                                }else {
                                    binding.noResultFoundLabel.setVisibility(View.GONE);
                                    binding.searchResultProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                return false;
            }
        });

        return binding.getRoot();
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);


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


    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//
//    }


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