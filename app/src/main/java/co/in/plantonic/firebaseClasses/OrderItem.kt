package co.`in`.plantonic.firebaseClasses

data class OrderItem(val orderId: String, val merchantId : String, val productId : String,
                     val customerId: String, val customerName: String, val address: String, val pincode: String, val addressType : String,
                     val phoneNo: String, val email: String, val paymentMethod: String, val quantity: Long, val status: String,
                     val transactionId: String, val timeStamp: Long, val payable : String, val listedPrice: String,
                     val deliveryCharge: String, val deliveryDate: Long, val special_instructions: String) : java.io.Serializable{
    constructor(): this("","","","","","", "", "",
        "","", "",1L, "", "", -1L, "", "", "", -1L, "")
}
