package co.`in`.plantonic.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.`in`.plantonic.firebaseClasses.OrderItem
import co.`in`.plantonic.repo.OrderRepository
import co.`in`.plantonic.repo.ProductDetailsRepo
import co.`in`.plantonic.retrofit.models.order.GetOrdersResponseModel
import co.`in`.plantonic.retrofit.models.track.TrackOrderResponseModel

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