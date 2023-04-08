package com.example.plantonic.Adapter.listeners;

import com.example.plantonic.firebaseClasses.ProductItem;

public interface CartListner {
    void onRemoveFromCartClicked(ProductItem productItem);
    void onCartItemClicked(ProductItem productItem);
}
