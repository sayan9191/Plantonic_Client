package com.example.myschoolwearclient.firebaseClasses

data class UserOrderItem(val orderId: String, val productId: String, val customerId: String, val merchantId: String){
    constructor(): this("", "", "", "")
}
