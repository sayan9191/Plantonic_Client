package com.example.plantonic.Adapter.listeners

import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.retrofit.models.order.Data

interface TrackOrderListener {
    fun onTrackOrderClicked(orderItem: Data)
    fun onOrderedProductClicked(productId: String)
}