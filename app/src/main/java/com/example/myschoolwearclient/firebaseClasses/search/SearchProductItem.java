package com.example.myschoolwearclient.firebaseClasses.search;

import java.util.ArrayList;
import java.util.List;

public class SearchProductItem {

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getSearch_keyword() {
        return search_keyword;
    }

    public void setSearch_keyword(List<String> search_keyword) {
        this.search_keyword = search_keyword;
    }

    public SearchProductItem(String productId, String productImageUrl, String productName, List<String> search_keyword) {
        this.productId = productId;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.search_keyword = search_keyword;
    }

    public SearchProductItem() {
        this.productId = "";
        this.productImageUrl = "";
        this.productName = "";
        this.search_keyword = new ArrayList<>();
    }

    String productId;
    String productImageUrl;
    String productName;
    List<String> search_keyword;
}
