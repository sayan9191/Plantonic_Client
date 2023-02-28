package com.example.myschoolwearclient.Adapter.listeners;

import com.example.myschoolwearclient.firebaseClasses.ProductItem;

public interface FavouriteListener {
    void onGoToCartBtnClicked(String productId);

    void onProductClicked(ProductItem productItem);
}
