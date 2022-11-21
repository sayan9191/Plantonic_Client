package com.example.plantonic.repo;

import static com.example.plantonic.utils.constants.DatabaseConstants.getParticularProductReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantonic.ui.firebaseClasses.ProductItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsRepo {
    private static ProductDetailsRepo repository;
    public static ProductDetailsRepo getInstance() {
        if (repository ==null){
            repository = new ProductDetailsRepo();
        }
        return repository;
    }

    MutableLiveData<ProductItem> _productItem = new MutableLiveData();

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
}
