package com.example.plantonic.ui.homeFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.plantonic.CategoryAdapter;
import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.CategoryItem;
import com.example.plantonic.SearchFragment;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    RecyclerView recyclerView;
    ImageView searchBtn;
    View view;
    private HomeFragmentViewModel viewModel;

    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        imageSlider = view.findViewById(R.id.imageSlider);
        recyclerView= view.findViewById(R.id.recyclerView1);
        searchBtn = view.findViewById(R.id.searchBtn);

        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);


        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2014/02/27/16/10/flowers-276014__480.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__480.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2014/04/14/20/11/pink-324175__480.jpg", ScaleTypes.FIT));


        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        categoryAdapter = new CategoryAdapter(this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeFragment.this.requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(this.categoryAdapter);

        viewModel.allCategories.observe(getViewLifecycleOwner(), new Observer<ArrayList<CategoryItem>>() {
            @Override
            public void onChanged(ArrayList<CategoryItem> categoryItems) {
                categoryAdapter.updateCategories(categoryItems);

                Log.d("-----------", categoryItems.get(0).categoryName);
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, new SearchFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}