package com.example.plantonic.firebaseClasses

data class CategoryProductItem(val categoryId : String, val merchantId : String, val productId: String) {
    constructor() : this("", "", "")
}
