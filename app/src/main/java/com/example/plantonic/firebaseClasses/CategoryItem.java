package com.example.plantonic.firebaseClasses;

public class CategoryItem {
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String categoryId;
    public String categoryName;
    String categoryDescription;
    String categoryType;
    public String categoryImage;

    public CategoryItem(){
        this.categoryId = "";
        this.categoryName = "";
        this.categoryDescription = "";
        this.categoryType = "";
        this.categoryImage = "";
    }

    public CategoryItem(String categoryId, String categoryName, String categoryDescription, String categoryType, String categoryImageUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryType = categoryType;
        this.categoryImage = categoryImageUrl;
    }

}
