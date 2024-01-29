package co.in.plantonic.ui.cartfav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import co.in.plantonic.firebaseClasses.CartItem;
import co.in.plantonic.repo.CartRepository;

public class CartViewModel extends ViewModel {

    CartRepository cartRepository = new CartRepository();

    public LiveData[] getAllCartItems(String userId){
        cartRepository.getAllCartItems(userId);
        return new LiveData[] {cartRepository.allCartItems, cartRepository.allCartProducts};
    }


    public void removeFromCart(String userId, String productId){
        cartRepository.removeProductFromCart(userId, productId);
    }

    public void addIncreaseCartQuantity(String userId, String productId, Long quantity){
        cartRepository.increaseCartQuantity(new CartItem(userId, productId, quantity, System.currentTimeMillis()), 1L);
    }

    public void removeDecreaseCartQuantity(String userId, String productId, Long quantity){
        cartRepository.decreaseCartQuantity(new CartItem(userId, productId, quantity, System.currentTimeMillis()));
    }
}
