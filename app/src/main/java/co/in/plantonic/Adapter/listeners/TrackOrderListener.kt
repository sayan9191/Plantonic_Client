package co.`in`.plantonic.Adapter.listeners

import co.`in`.plantonic.retrofit.models.order.Data

interface TrackOrderListener {
    fun onTrackOrderClicked(orderItem: Data)
    fun onOrderedProductClicked(productId: String)
}