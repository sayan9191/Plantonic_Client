package com.example.plantonic.repo;


import static com.example.plantonic.utils.constants.DatabaseConstants.getParticularProductReference;
import static com.example.plantonic.utils.constants.DatabaseConstants.getSpecificUserCartItemReference;
import static com.example.plantonic.utils.constants.DatabaseConstants.getUserFavouriteProductReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.FavouriteItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProductDetailsRepo {
    private static ProductDetailsRepo repository;
    public static ProductDetailsRepo getInstance() {
        if (repository ==null){
            repository = new ProductDetailsRepo();
        }
        return repository;
    }

    MutableLiveData<ProductItem> _productItem = new MutableLiveData<>();

    public LiveData<ProductItem> productItem = _productItem;

    public void getProductFromId(String product) {
        getParticularProductReference(product).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItem item = snapshot.getValue(ProductItem.class);
                    if (item != null){
                        _productItem.postValue(item);

                        Log.d("------------------", item.productName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void addToFav(FavouriteItem favouriteItem){
        getUserFavouriteProductReference(favouriteItem.getUserId(), favouriteItem.getProductId())
                .setValue(favouriteItem)
                .addOnSuccessListener(unused -> {
                    Log.d("FAV", "FAV item Added");
                    _isFav.postValue(true);
                });
    }

    public void removeFav(String userId, String productId){
        try {
            getUserFavouriteProductReference(userId, productId)
                    .removeValue().addOnSuccessListener(unused -> {
                        Log.d("FAV", "Fav item removed");
                        _isFav.postValue(false);
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Checking if item is in favourites or not
     */
    MutableLiveData<Boolean> _isFav = new MutableLiveData<>();
    public LiveData<Boolean> isFav = _isFav;

    public void checkIfAddedToFav(String userId, String productId){
        getUserFavouriteProductReference(userId, productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            _isFav.postValue(true);
                        }else {
                            _isFav.postValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        _isFav.postValue(false);
                    }
                });
    }


    /**
     * Checking if item is in favourites or not
     */
    MutableLiveData<Boolean> _isAddedToCart = new MutableLiveData<>();
    public LiveData<Boolean> isAddedToCart = _isAddedToCart;

    public void checkIfAddedToCart(String userId, String productId){
        getSpecificUserCartItemReference(userId, productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            _isAddedToCart.postValue(true);
                        }else {
                            _isAddedToCart.postValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        _isAddedToCart.postValue(false);
                    }
                });
    }



    MutableLiveData<CartItem> _currentCartQuantity = new MutableLiveData<>();
    public LiveData<CartItem> currentCartQuantity = _currentCartQuantity;

    public void grtCurrentCartQuantity(String userId, String productId){
        getSpecificUserCartItemReference(userId, productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            CartItem cartItem = snapshot.getValue(CartItem.class);
                            if (cartItem!=null && Objects.equals(cartItem.getProductId(), productId)){
                                _currentCartQuantity.postValue(cartItem);
                            }else {
                                _currentCartQuantity.postValue(null);
                            }
                        }else {
                            _currentCartQuantity.postValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
