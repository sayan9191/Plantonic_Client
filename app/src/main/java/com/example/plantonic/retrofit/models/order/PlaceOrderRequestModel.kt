package com.example.plantonic.retrofit.models.order

import com.example.plantonic.firebaseClasses.OrderItem

data class PlaceOrderRequestModel(
    val all_orders : List<OrderItem>
)
