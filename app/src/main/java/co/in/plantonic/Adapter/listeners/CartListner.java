package co.in.plantonic.Adapter.listeners;

import co.in.plantonic.firebaseClasses.ProductItem;

public interface CartListner {
    void onRemoveFromCartClicked(ProductItem productItem);
    void onCartItemClicked(ProductItem productItem);
}
