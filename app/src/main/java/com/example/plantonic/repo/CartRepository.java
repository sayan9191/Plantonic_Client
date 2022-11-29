package com.example.plantonic.repo;

import static com.example.plantonic.utils.constants.DatabaseConstants.getSpecificUserCartItemReference;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.plantonic.firebaseClasses.CartItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CartRepository {
    private String TAG = "CartRepo";


    public void addToCart(CartItem cartItem){
        getSpecificUserCartItemReference(cartItem.getUserId(), cartItem.getProductId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            increaseCartQuantity(snapshot, cartItem);
                        }else{
                            addProductToCart(cartItem);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void increaseCartQuantity(DataSnapshot snapshot, CartItem cartItem) {
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
}
