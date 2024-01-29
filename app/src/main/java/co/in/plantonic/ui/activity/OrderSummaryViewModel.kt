package co.`in`.plantonic.ui.activity

import androidx.lifecycle.ViewModel
import co.`in`.plantonic.repo.CartRepository
import co.`in`.plantonic.repo.OrderRepository
import androidx.lifecycle.LiveData
import co.`in`.plantonic.firebaseClasses.OrderItem
import co.`in`.plantonic.retrofit.models.order.PlaceOrderResponseModel

class OrderSummaryViewModel : ViewModel() {
    var cartRepository = CartRepository()
    var orderRepository = OrderRepository()
    fun getAllOrderSummaryItems(userId: String?): Array<LiveData<*>> {
        cartRepository.getAllCartItems(userId)
        return arrayOf(cartRepository.allCartItems, cartRepository.allCartProducts)
    }

    //    public void placeOrder(OrderItem orderItem){
    //        orderRepository.updateOrder(orderItem);
    //    }
    val isLoading: LiveData<Boolean> = orderRepository.isLoading
    val errorMessage: LiveData<String> = orderRepository.errorMessage
    fun placeOrder(allOrders: List<OrderItem>): LiveData<PlaceOrderResponseModel> {
        orderRepository.placeOrder(allOrders)
        return orderRepository.placeOrderResponse
    }

}