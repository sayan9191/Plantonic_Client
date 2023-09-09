package com.example.plantonic.repo;

import static com.example.plantonic.utils.constants.DatabaseConstants.getAllUserFavouritesReference;
import static com.example.plantonic.utils.constants.DatabaseConstants.getParticularProductReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.FavouriteItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavouriteRepository {

    MutableLiveData<List<ProductItem>> _allFavItems = new MutableLiveData<>();
    public LiveData<List<ProductItem>> allFavItems = _allFavItems;


    HashMap<String, ProductItem> favProductMap = new HashMap<>();

    public void getAllUserFavouriteItem(String userId){
        getAllUserFavouritesReference(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            getAllUserFavouritesReference(userId).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.exists()){
                                        FavouriteItem item = snapshot.getValue(FavouriteItem.class);
                                        if (item != null){
                                            getProductFromId(item.getProductId());
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.exists()){
                                        FavouriteItem item = snapshot.getValue(FavouriteItem.class);
                                        if (item != null){
                                            getProductFromId(item.getProductId());
                                        }
                                    }
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                    if (favProductMap.containsKey(snapshot.getKey())){
                                        favProductMap.remove(snapshot.getKey());
                                        _allFavItems.postValue(new ArrayList<>(favProductMap.values()));
                                    }
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Database error: ", error.getDetails());
                                }
                            });
                        }else {
                            _allFavItems.postValue(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        _allFavItems.postValue(new ArrayList<>());
                    }
                });

    }

    private void getProductFromId(String productId) {
        getParticularProductReference(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItem item = snapshot.getValue(ProductItem.class);
                    if (item != null){
                        favProductMap.put(snapshot.getKey(), item);
                        _allFavItems.postValue(new ArrayList<>(favProductMap.values()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
