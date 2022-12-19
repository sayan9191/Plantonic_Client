package com.example.plantonic.firebaseClasses;

public class ProductItem {
    public String productId;
    public String merchantId;
    public String productName;
    public String productDescription;
    public String listedPrice;
    public String actualPrice;
    public String imageUrl1;
    public String imageUrl2;
    public String imageUrl3;
    private String imageUrl4;
    private Long currentStock;
    private String deliveryCharge;
    private String category;
    private Long timestamp;

    public ProductItem(String category, Long timestamp) {
        this.category = category;
        this.timestamp = timestamp;
        this.productId = "";
        this.merchantId = "";
        this.productName = "";
        this.productDescription = "";
        this.listedPrice = "";
        this.actualPrice = "";
        this.imageUrl1 = "";
        this.imageUrl2 = "";
        this.imageUrl3 = "";
        this.imageUrl4 = "";
        this.currentStock = 0L;
        this.deliveryCharge = "";
    }

    public ProductItem(String productId, String merchantId, String productName, String productDescription, String listedPrice, String actualPrice, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, Long currentStock, String deliveryCharge, String category, Long timestamp) {
        this.productId = productId;
        this.merchantId = merchantId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.listedPrice = listedPrice;
        this.actualPrice = actualPrice;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.currentStock = currentStock;
        this.deliveryCharge = deliveryCharge;
        this.category = category;
        this.timestamp = timestamp;
    }

    public Long getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Long currentStock) {
        this.currentStock = currentStock;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getListedPrice() {
        return listedPrice;
    }

    public void setListedPrice(String listedPrice) {
        this.listedPrice = listedPrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
