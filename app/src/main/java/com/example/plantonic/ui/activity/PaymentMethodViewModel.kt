package com.example.plantonic.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.repo.CartRepository
import com.example.plantonic.repo.OrderRepository
import com.example.plantonic.retrofit.models.order.PlaceOrderResponseModel

class PaymentMethodViewModel : ViewModel() {

    var orderRepository = OrderRepository()

    val isLoading: LiveData<Boolean> = orderRepository.isLoading
    val errorMessage: LiveData<String> = orderRepository.errorMessage
    fun placeOrder(allOrders: List<OrderItem>): LiveData<PlaceOrderResponseModel> {
        orderRepository.placeOrder(allOrders)
        return orderRepository.placeOrderResponse
    }
}