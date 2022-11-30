package com.example.plantonic.ui.cartfav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.repo.CartRepository;

import java.util.List;

public class CartViewModel extends ViewModel {

    CartRepository cartRepository = new CartRepository();

    public LiveData[] getAllCartItems(String userId){
        cartRepository.getAllCartItems(userId);
        return new LiveData[] {cartRepository.allCartItems, cartRepository.allCartProducts};
    }
}
