package com.example.plantonic.repo

import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.firebaseClasses.UserOrderItem
import com.example.plantonic.utils.constants.DatabaseConstants.*

class OrderRepository {

    private val cartRepository = CartRepository()

    fun updateOrder(orderItem: OrderItem){
        getSpecificOrderReference(orderItem.orderId)
            .setValue(orderItem).addOnSuccessListener {
                updateMerchantOrder(UserOrderItem(orderItem.orderId, orderItem.productId, orderItem.customerId, orderItem.merchantId))
            }
    }

    private fun updateMerchantOrder(orderItem: UserOrderItem){
        getSpecificMerchantOrderReference(orderItem.merchantId, orderItem.orderId)
            .setValue(orderItem).addOnSuccessListener {
                updateUserOrder(orderItem)
            }
    }

    private fun updateUserOrder(userOrderItem: UserOrderItem){
        getSpecificUserOrderReference(userOrderItem.customerId, userOrderItem.orderId)
            .setValue(userOrderItem).addOnSuccessListener {
                cartRepository.removeProductFromCart(userOrderItem.customerId, userOrderItem.productId)
            }
    }

}