package com.example.myschoolwearclient.firebaseClasses;

public class CartItem {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    String userId;
    String productId;
    Long quantity;
    Long timeStamp;

    public CartItem() {
        this.userId = "";
        this.productId = "";
        this.quantity = 1L;
        this.timeStamp = -1L;
    }

    public CartItem(String userId, String productId, Long quantity, Long timeStamp) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
    }
}
