package com.example.plantonic.utils.constants;

import com.example.plantonic.ui.firebaseClasses.FavouriteItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConstants {
    private static final String CATEGORY = "categories";
    private static final String POPULAR_PRODUCTS = "popularProducts";
    private static final String PRODUCTS = "products";
    private static final String FAVOURITES = "fav";

    public static DatabaseReference getAllCategoriesReference(){
        return FirebaseDatabase.getInstance().getReference(CATEGORY);
    }

    public static DatabaseReference getAllPopularProductsReference(){
        return FirebaseDatabase.getInstance().getReference(POPULAR_PRODUCTS);
    }

    public static DatabaseReference getParticularProductReference(String productId) {
        return FirebaseDatabase.getInstance().getReference(PRODUCTS).child(productId);
    }

    public static  DatabaseReference getAllUserFavouritesReference(String userId){
        return FirebaseDatabase.getInstance().getReference(FAVOURITES).child(userId);
    }

    public static  DatabaseReference getUserFavouriteProductReference(String userId, String productId){
        return FirebaseDatabase.getInstance().getReference(FAVOURITES).child(userId).child(productId);
    }
}
