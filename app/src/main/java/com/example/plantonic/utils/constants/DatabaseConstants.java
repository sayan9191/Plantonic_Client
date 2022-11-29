package com.example.plantonic.utils.constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConstants {
    private static final String CATEGORY = "categories";
    private static final String POPULAR_PRODUCTS = "popularProducts";
    private static final String PRODUCTS = "products";
    private static final String FAVOURITES = "fav";
    private static final String CART = "cart";

    public static DatabaseReference getAllCategoriesReference(){
        return FirebaseDatabase.getInstance().getReference(CATEGORY);
    }

    public static DatabaseReference getAllPopularProductsReference(){
        return FirebaseDatabase.getInstance().getReference(POPULAR_PRODUCTS);
    }

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
}
