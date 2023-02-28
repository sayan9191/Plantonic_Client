package com.example.myschoolwearclient.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myschoolwearclient.firebaseClasses.OrderItem
import com.example.myschoolwearclient.repo.OrderRepository
import com.example.myschoolwearclient.repo.ProductDetailsRepo

class OrdersViewModel : ViewModel() {
    private val orderRepository : OrderRepository = OrderRepository()
    private val productRepository : ProductDetailsRepo = ProductDetailsRepo()

    fun getAllOrders(userId: String) : LiveData<List<OrderItem>> {
        orderRepository.getAllUserOrders(userId)
        return orderRepository.allOrderItems
    }

}