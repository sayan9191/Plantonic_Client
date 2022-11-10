package com.example.plantonic;

import android.view.View;

import com.example.plantonic.firebaseClasses.ProductItem;

public interface OnProductListener {
    void onProductClick(ProductItem productItem);
}
