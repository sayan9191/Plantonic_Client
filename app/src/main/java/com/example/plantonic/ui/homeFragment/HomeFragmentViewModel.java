package com.example.plantonic.ui.homeFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.CategoryItem;
import com.example.plantonic.repo.Repository;

import java.util.ArrayList;

public class HomeFragmentViewModel extends ViewModel {

    Repository repository = Repository.getInstance();

    LiveData<ArrayList<CategoryItem>> allCategories = repository.getCategories();


}
