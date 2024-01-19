package com.example.plantonic.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.repo.OrderRepository
import com.example.plantonic.repo.ProductDetailsRepo
import com.example.plantonic.retrofit.models.order.GetOrdersResponseModel
import com.example.plantonic.retrofit.models.track.TrackOrderResponseModel

class OrdersViewModel : ViewModel() {
    private val orderRepository : OrderRepository = OrderRepository()
    private val productRepository : ProductDetailsRepo = ProductDetailsRepo()

    val isLoading: LiveData<Boolean> = orderRepository.isLoading
    val errorMessage: LiveData<String> = orderRepository.errorMessage

    fun getAllOrders(userId: String) : LiveData<List<OrderItem>> {
        orderRepository.getAllUserOrders(userId)
        return orderRepository.allOrderItems
    }

    val myOrders: LiveData<GetOrdersResponseModel> = orderRepository.myOrdersResponseModel

    fun getMyOrders(page: Int) {
        orderRepository.getMyOrders(page)
    }

    val trackOrderLiveData : LiveData<TrackOrderResponseModel> = orderRepository.trackOrderResponse
    fun trackOrder(orderId: Int) {
        orderRepository.trackOrder(orderId)
    }

}