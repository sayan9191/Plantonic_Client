package com.example.plantonic.repo;

import static com.example.plantonic.utils.constants.DatabaseConstants.getAllCategoriesReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.CategoryItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Repository {

    private static Repository repository;
    public static Repository getInstance() {
        if (repository ==null){
            repository = new Repository();
        }
        return repository;
    }


    private MutableLiveData<ArrayList<CategoryItem>> categoryItems = new MutableLiveData<ArrayList<CategoryItem>>();

    private HashMap<String,CategoryItem> hashMap= new HashMap<>();

    public LiveData<ArrayList<CategoryItem>> getCategories(){
        getAllCategories();
        return categoryItems;
    }

    private void getAllCategories(){
        getAllCategoriesReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    CategoryItem item = snapshot.getValue(CategoryItem.class);
                    if (item!= null){
                        hashMap.put(snapshot.getKey(), item);
//                        Collections.sort(categoryItems, categoryItems.);
                        categoryItems.postValue(new ArrayList<CategoryItem>(hashMap.values()));
                        Log.d("_________", item.categoryName);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    CategoryItem item = snapshot.getValue(CategoryItem.class);
                    if (item!= null){
                        hashMap.put(snapshot.getKey(), item);
                        Log.d("-----------", item.categoryName);
                        categoryItems.postValue(new ArrayList<CategoryItem>(hashMap.values()));
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (hashMap.containsKey(snapshot.getKey())){
                    hashMap.remove(snapshot.getKey());
                    categoryItems.postValue(new ArrayList<CategoryItem>(hashMap.values()));
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
