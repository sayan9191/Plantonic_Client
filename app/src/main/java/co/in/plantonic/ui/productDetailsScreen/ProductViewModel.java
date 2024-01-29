package co.in.plantonic.ui.productDetailsScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import co.in.plantonic.firebaseClasses.CartItem;
import co.in.plantonic.repo.CartRepository;
import co.in.plantonic.repo.ProductDetailsRepo;
import co.in.plantonic.firebaseClasses.FavouriteItem;
import co.in.plantonic.firebaseClasses.ProductItem;

public class ProductViewModel extends ViewModel {
    private final ProductDetailsRepo repository = ProductDetailsRepo.getInstance();

    private final LiveData<ProductItem> productItem = repository.productItem;
    private final CartRepository cartRepository = new CartRepository();

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

    public void addToCart(String userId, String productId, Long quantity){
        CartItem item = new CartItem(userId, productId, quantity, System.currentTimeMillis());
        cartRepository.addToCart(item);
    }

    public LiveData<Boolean> checkIfAddedToCart(String userId, String productId){
        repository.checkIfAddedToCart(userId, productId);
        return repository.isAddedToCart;
    }

//    public LiveData<CartItem> currentCartQuantity = repository.currentCartQuantity;
//    public void getCurrentCartQuantity(String userId, String productId){
//        repository.grtCurrentCartQuantity(userId, productId);
//    }

}