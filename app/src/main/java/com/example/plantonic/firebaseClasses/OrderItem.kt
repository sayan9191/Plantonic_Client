package com.example.plantonic.firebaseClasses

data class OrderItem(val orderId: String, val merchantId : String, val productId : String,
                     val customerId: String, val customerName: String, val address: String, val addressType : String,
                     val phoneNo: String, val paymentMethod: String, val quantity: Long, val status: String,
                     val transactionId: String, val timeStamp: Long, val payable : String, val listedPrice: String,
                     val deliveryCharge: String){
    constructor(): this("","","","","","","",
        "","",1L, "", "", -1L, "", "", "")
}
