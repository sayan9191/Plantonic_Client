package com.example.plantonic.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.repo.OrderRepository

class OrdersViewModel : ViewModel() {
    private val orderRepository : OrderRepository = OrderRepository()

    fun getAllOrders(userId: String) : LiveData<List<OrderItem>> {
        orderRepository.getAllUserOrders(userId)
        return orderRepository.allOrderItems
    }
}