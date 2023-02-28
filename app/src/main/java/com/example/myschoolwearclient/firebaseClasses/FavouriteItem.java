package com.example.myschoolwearclient.firebaseClasses;

public class FavouriteItem {
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public FavouriteItem(String userId, String productId, String merchantId, Long timeStamp) {
        this.userId = userId;
        this.productId = productId;
        this.merchantId = merchantId;
        this.timeStamp = timeStamp;
    }

    public FavouriteItem() {
        this.userId = "";
        this.productId = "";
        this.merchantId = "";
        this.timeStamp = (long) -1;
    }

    String userId;
    String productId;
    String merchantId;
    Long timeStamp;

}
