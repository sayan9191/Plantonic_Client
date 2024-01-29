package co.`in`.plantonic.retrofit.models.order

import co.`in`.plantonic.firebaseClasses.OrderItem

data class PlaceOrderRequestModel(
    val all_orders : List<OrderItem>
)
