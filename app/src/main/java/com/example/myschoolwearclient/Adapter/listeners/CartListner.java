package com.example.myschoolwearclient.Adapter.listeners;

import com.example.myschoolwearclient.firebaseClasses.ProductItem;

public interface CartListner {
    void onRemoveFromCartClicked(ProductItem productItem);
    void onCartItemClicked(ProductItem productItem);
}
