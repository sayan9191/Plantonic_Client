package com.example.plantonic.ui.productDetailsScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.repo.ProductDetailsRepo;
import com.example.plantonic.ui.firebaseClasses.FavouriteItem;
import com.example.plantonic.ui.firebaseClasses.ProductItem;

public class ProductViewModel extends ViewModel {
    private final ProductDetailsRepo repository = ProductDetailsRepo.getInstance();

    private final LiveData<ProductItem> productItem = repository.productItem;

    LiveData<ProductItem> getProductDetailsFromId(String id){
        repository.getProductFromId(id);
        return productItem;
    }

    public void addToFav(FavouriteItem favouriteItem){
        repository.addToFav(favouriteItem);
    }

    public LiveData<Boolean> checkIfFav(String userId, String productId){
        repository.checkIfAddedToFav(userId, productId);
        return repository.isFav;
    }

    public void removeFromFav(String userId, String productId){
        repository.removeFav(userId, productId);
    }
}
