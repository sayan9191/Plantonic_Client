package com.example.plantonic.utils.constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseConstants {
    private static final String CATEGORY = "categories";
    private static final String POPULAR_PRODUCTS = "popularProducts";
    private static final String PRODUCTS = "products";

    public static DatabaseReference getAllCategoriesReference(){
        return FirebaseDatabase.getInstance().getReference(CATEGORY);
    }

    public static DatabaseReference getAllPopularProductsReference(){
        return FirebaseDatabase.getInstance().getReference(POPULAR_PRODUCTS);
    }

    public static DatabaseReference getParticularProductReference(String productId) {
        return FirebaseDatabase.getInstance().getReference(PRODUCTS).child(productId);
    }
}
