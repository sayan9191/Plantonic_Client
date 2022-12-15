package com.example.plantonic.firebaseClasses

data class OrderItem(val orderId: String, val merchantId : String, val productId : String,
                     val customerId: String, val customerName: String, val address: String,
                     val phoneNo: String, val paymentMethod: String, val quantity: String, val status: String,
                     val transactionId: String)
