package com.example.plantonic.utils.constants;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConstants {
    private static final String CATEGORY = "categories";
    private static final String POPULAR_PRODUCTS = "popularProducts";
    private static final String PRODUCTS = "products";
    private static final String FAVOURITES = "fav";
    private static final String CART = "cart";
    private static final String ADDRESS = "address";
    private static final String ORDERS = "orders";
    private static final String USER_ORDERS = "userOrders";
    private static final String MERCHANT_ORDERS = "merchantOrders";
    private static final String CATEGORY_ITEMS = "categoryItems";


    // Categories
    public static DatabaseReference getAllCategoriesReference(){
        return FirebaseDatabase.getInstance().getReference(CATEGORY);
    }

    // Individual Categories
    public static DatabaseReference getParticularCategoryItems(String categoryId){
        return FirebaseDatabase.getInstance().getReference(CATEGORY_ITEMS).child(categoryId);
    }

    // Popular product
    public static DatabaseReference getAllPopularProductsReference(){
        return FirebaseDatabase.getInstance().getReference(POPULAR_PRODUCTS);
    }

    // Product
    public static DatabaseReference getParticularProductReference(String productId) {
        return FirebaseDatabase.getInstance().getReference(PRODUCTS).child(productId);
    }

    // Favourites

    public static DatabaseReference getAllUserFavouritesReference(String userId){
        return FirebaseDatabase.getInstance().getReference(FAVOURITES).child(userId);
    }

    public static DatabaseReference getUserFavouriteProductReference(String userId, String productId){
        return FirebaseDatabase.getInstance().getReference(FAVOURITES).child(userId).child(productId);
    }

    // Cart

    public static DatabaseReference getAllUserCartItemsReference(String userId){
        return FirebaseDatabase.getInstance().getReference(CART).child(userId);
    }

    public static DatabaseReference getSpecificUserCartItemReference(String userId, String productId){
        return FirebaseDatabase.getInstance().getReference(CART).child(userId).child(productId);
    }

    // Address
    public static DatabaseReference getUserAddressReference(String userId){
        return FirebaseDatabase.getInstance().getReference(ADDRESS).child(userId);
    }

    // Orders
    public static DatabaseReference getAllOrdersReference(){
        return FirebaseDatabase.getInstance().getReference(ORDERS);
    }

    public static DatabaseReference getSpecificOrderReference(String orderId){
        return FirebaseDatabase.getInstance().getReference(ORDERS).child(orderId);
    }

    public static DatabaseReference getAllUserOrdersReference(String userId){
        return FirebaseDatabase.getInstance().getReference(USER_ORDERS).child(userId);
    }

    public static DatabaseReference getSpecificUserOrderReference(String userId, String orderId){
        return FirebaseDatabase.getInstance().getReference(USER_ORDERS).child(userId).child(orderId);
    }

    public static DatabaseReference getSpecificMerchantOrderReference(String merchantId, String orderId){
        return FirebaseDatabase.getInstance().getReference(MERCHANT_ORDERS).child(merchantId).child(orderId);
    }
}
