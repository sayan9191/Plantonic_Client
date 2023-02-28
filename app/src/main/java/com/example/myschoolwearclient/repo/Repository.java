package com.example.myschoolwearclient.repo;

import static com.example.myschoolwearclient.utils.constants.DatabaseConstants.getAllCategoriesReference;
import static com.example.myschoolwearclient.utils.constants.DatabaseConstants.getAllPopularProductsReference;
import static com.example.myschoolwearclient.utils.constants.DatabaseConstants.getParticularProductReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myschoolwearclient.firebaseClasses.CategoryItem;
import com.example.myschoolwearclient.firebaseClasses.PopularProductItem;
import com.example.myschoolwearclient.firebaseClasses.ProductItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repository {

    private static Repository repository;
    public static Repository getInstance() {
        if (repository ==null){
            repository = new Repository();
        }
        return repository;
    }


    private MutableLiveData<ArrayList<CategoryItem>> categoryItems = new MutableLiveData<ArrayList<CategoryItem>>();

    private MutableLiveData<List<PopularProductItem>> allPopularProducts = new MutableLiveData<>();

    private HashMap<String,CategoryItem> hashMap= new HashMap<>();

    private MutableLiveData<ArrayList<ProductItem>> allPopularProductItems = new MutableLiveData<>();

    private HashMap<String, ProductItem> productItemsHashMap = new HashMap<>();


    public LiveData<ArrayList<CategoryItem>> getCategories(){
        getAllCategories();
        return categoryItems;
    }

    public MutableLiveData<List<PopularProductItem>> getALlPopularProducts(){
        getAllPopularProducts();
        return allPopularProducts;
    }

    public LiveData<ArrayList<ProductItem>> getPopularProductItems(){
        getAllPopularProducts();
        return allPopularProductItems;
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

    private HashMap<String, PopularProductItem> popularHashMap = new HashMap<String, PopularProductItem>();

    private void getAllPopularProducts(){
        getAllPopularProductsReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    PopularProductItem item = snapshot.getValue(PopularProductItem.class);
                    if (item != null){
                        popularHashMap.put(snapshot.getKey(), item);
                        getProductFromId(item.getProductId());

                        Log.d("------------------", item.productId);
                        allPopularProducts.postValue(new ArrayList<PopularProductItem>(popularHashMap.values()));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    PopularProductItem item = snapshot.getValue(PopularProductItem.class);
                    if (item != null){
                        popularHashMap.put(snapshot.getKey(), item);
                        getProductFromId(item.getProductId());
                        allPopularProducts.postValue(new ArrayList<PopularProductItem>(popularHashMap.values()));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (popularHashMap.containsKey(snapshot.getKey())){
                    popularHashMap.remove(snapshot.getKey());

                    allPopularProducts.postValue(new ArrayList<PopularProductItem>(popularHashMap.values()));
                    if (productItemsHashMap.containsKey(snapshot.getKey())){
                        productItemsHashMap.remove(snapshot.getKey());
                    }
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


    public void getProductFromId(String product) {
        getParticularProductReference(product).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItem item = snapshot.getValue(ProductItem.class);
                    if (item != null){
                        productItemsHashMap.put(snapshot.getKey(), item);
                        allPopularProductItems.postValue(new ArrayList<>(productItemsHashMap.values()));
                        Log.d("------------------", item.productName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
