package com.example.plantonic.Adapter.listeners;

import com.example.plantonic.firebaseClasses.ProductItem;

public interface FavouriteListener {
    void onGoToCartBtnClicked(String productId);

    void onProductClicked(ProductItem productItem);
}
