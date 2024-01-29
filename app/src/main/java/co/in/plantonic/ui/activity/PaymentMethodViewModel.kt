package co.`in`.plantonic.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.`in`.plantonic.firebaseClasses.OrderItem
import co.`in`.plantonic.repo.OrderRepository
import co.`in`.plantonic.retrofit.models.order.PlaceOrderResponseModel

class PaymentMethodViewModel : ViewModel() {

    var orderRepository = OrderRepository()

    val isLoading: LiveData<Boolean> = orderRepository.isLoading
    val errorMessage: LiveData<String> = orderRepository.errorMessage
    fun placeOrder(allOrders: List<OrderItem>): LiveData<PlaceOrderResponseModel> {
        orderRepository.placeOrder(allOrders)
        return orderRepository.placeOrderResponse
    }
}