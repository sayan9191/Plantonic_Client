package co.in.plantonic.Adapter.listeners;

import co.in.plantonic.firebaseClasses.ProductItem;

public interface FavouriteListener {
    void onGoToCartBtnClicked(String productId);

    void onProductClicked(ProductItem productItem);

    void onAddToCartClicked(ProductItem productItem, int position);
}
