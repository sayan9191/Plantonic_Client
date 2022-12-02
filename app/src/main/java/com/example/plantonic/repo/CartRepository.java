package com.example.plantonic.repo;

import static com.example.plantonic.utils.constants.DatabaseConstants.getAllUserCartItemsReference;
import static com.example.plantonic.utils.constants.DatabaseConstants.getParticularProductReference;
import static com.example.plantonic.utils.constants.DatabaseConstants.getSpecificUserCartItemReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartRepository {
    private String TAG = "CartRepo";

    MutableLiveData<List<CartItem>> _allCartItems = new MutableLiveData<>();
    public LiveData<List<CartItem>> allCartItems = _allCartItems;

    HashMap<String, CartItem> cartMap = new HashMap<>();

    public void getAllCartItems(String userId){
        getAllUserCartItemsReference(userId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            CartItem item = snapshot.getValue(CartItem.class);
                            if (item != null){
                                cartMap.put(snapshot.getKey(), item);
                                _allCartItems.postValue(new ArrayList<>(cartMap.values()));
                                getCartProductsFromId(item.getProductId());
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            CartItem item = snapshot.getValue(CartItem.class);
                            if (item != null){
                                cartMap.put(snapshot.getKey(), item);
                                _allCartItems.postValue(new ArrayList<>(cartMap.values()));
                                getCartProductsFromId(item.getProductId());
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        if (cartMap.containsKey(snapshot.getKey())){
                            cartMap.remove(snapshot.getKey());
                            _allCartItems.postValue(new ArrayList<>(cartMap.values()));

                            cartProductMap.remove(snapshot.getKey());
                            _allCartProducts.postValue(new ArrayList<>(cartProductMap.values()));
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





    private final MutableLiveData<List<ProductItem>> _allCartProducts = new MutableLiveData<>();
    public LiveData<List<ProductItem>> allCartProducts = _allCartProducts;

    private HashMap<String, ProductItem> cartProductMap = new HashMap<>();

    private void getCartProductsFromId(String productId){
        getParticularProductReference(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItem item = snapshot.getValue(ProductItem.class);
                    if (item != null){
                        cartProductMap.put(snapshot.getKey(), item);
                        _allCartProducts.postValue(new ArrayList<>(cartProductMap.values()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void addToCart(CartItem cartItem){
        getSpecificUserCartItemReference(cartItem.getUserId(), cartItem.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            addProductToCart(cartItem);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void increaseCartQuantity(CartItem cartItem) {

        getSpecificUserCartItemReference(cartItem.getUserId(), cartItem.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Long quantity = (Long) snapshot.child("quantity").getValue();
                            if (quantity != null){
                                snapshot.child("quantity").getRef().setValue(quantity + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "increased quantity");
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void decreaseCartQuantity(CartItem cartItem) {
        getSpecificUserCartItemReference(cartItem.getUserId(), cartItem.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            CartItem cartItem = snapshot.getValue(CartItem.class);
                            if (cartItem!=null){
                                Long quantity = cartItem.getQuantity();
                                snapshot.child("quantity").getRef().setValue(quantity-1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void addProductToCart(CartItem cartItem){
        getSpecificUserCartItemReference(cartItem.getUserId(), cartItem.getProductId())
                .setValue(cartItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "added to cart");

                    }
                });
    }

    public void removeProductFromCart(String userId,String productId){
        getSpecificUserCartItemReference(userId, productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"Removed from Cart");
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
