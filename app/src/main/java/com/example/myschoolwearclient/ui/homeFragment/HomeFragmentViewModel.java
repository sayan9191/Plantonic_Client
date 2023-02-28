package com.example.myschoolwearclient.ui.homeFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myschoolwearclient.firebaseClasses.CategoryItem;
import com.example.myschoolwearclient.firebaseClasses.PopularProductItem;
import com.example.myschoolwearclient.firebaseClasses.ProductItem;
import com.example.myschoolwearclient.repo.Repository;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {

    Repository repository = Repository.getInstance();

    LiveData<ArrayList<CategoryItem>> allCategories = repository.getCategories();

    private LiveData<List<PopularProductItem>> allPopularProducts = repository.getALlPopularProducts();

    LiveData<ArrayList<ProductItem>> allPopularProductItems = repository.getPopularProductItems();
}
