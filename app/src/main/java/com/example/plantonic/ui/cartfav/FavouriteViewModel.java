package com.example.plantonic.ui.cartfav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.repo.CartRepository;
import com.example.plantonic.repo.FavouriteRepository;
import com.example.plantonic.repo.ProductDetailsRepo;
import com.example.plantonic.firebaseClasses.ProductItem;

import java.util.List;

public class FavouriteViewModel extends ViewModel {

    private final FavouriteRepository favouriteRepository = new FavouriteRepository();
    private final ProductDetailsRepo productDetailsRepo = new ProductDetailsRepo();
    private final CartRepository cartRepository = new CartRepository();

    public LiveData<List<ProductItem>> getAllFavItems(String userId){
        favouriteRepository.getAllUserFavouriteItem(userId);
        return favouriteRepository.allFavItems;
    }

    public void removeFromFav(String userId, String productId){
        productDetailsRepo.removeFav(userId, productId);
    }

    public void addToCart(String userId, String productId){
        CartItem item = new CartItem(userId, productId, 1L, System.currentTimeMillis());
        cartRepository.addToCart(item);
        productDetailsRepo.removeFav(userId, productId);
    }
}
