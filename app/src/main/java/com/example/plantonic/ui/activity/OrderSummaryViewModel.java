package com.example.plantonic.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.repo.CartRepository;

public class OrderSummaryViewModel extends ViewModel {

    CartRepository cartRepository = new CartRepository();

    public LiveData[] getAllOrderSummaryItems(String userId){
        cartRepository.getAllCartItems(userId);
        return new LiveData[] {cartRepository.allCartItems, cartRepository.allCartProducts};
    }
}
