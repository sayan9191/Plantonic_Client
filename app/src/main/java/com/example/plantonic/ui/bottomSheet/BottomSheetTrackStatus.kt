package com.example.plantonic.ui.bottomSheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantonic.Adapter.TrackStatusAdapter
import com.example.plantonic.R
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.retrofit.models.order.Data
import com.example.plantonic.retrofit.models.track.TrackOrderResponseModel
import com.example.plantonic.utils.constants.DatabaseConstants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class BottomSheetTrackStatus constructor(private var shipmentResponse: TrackOrderResponseModel, private var order : Data) :
    BottomSheetDialogFragment() {

//    companion object{
//        private var instance: BottomSheetTrackStatus? = null
//        fun getInstance(shipmentRes: TrackOrderResponseModel) : BottomSheetTrackStatus {
//            return if (instance == null){
//                instance = BottomSheetTrackStatus(shipmentRes)
//                instance as BottomSheetTrackStatus
//            } else{
//                instance?.shipmentResponse = shipmentRes
//                instance as BottomSheetTrackStatus
//            }
//        }
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_track_status, container, false)

        val trackingIdTextView = view.findViewById<TextView>(R.id.trackingIdTextView)
        val trackItemImageView = view.findViewById<ImageView>(R.id.trackItemImageView)
        val trackItemProductName = view.findViewById<TextView>(R.id.trackItemProductName)
        val trackItemQuantity = view.findViewById<TextView>(R.id.trackItemQuantity)
        val trackOrderId = view.findViewById<TextView>(R.id.trackOrderId)
        val trackDeliveryChargeTextView = view.findViewById<TextView>(R.id.trackDeliveryChargeTextView)
        val trackItemPrice = view.findViewById<TextView>(R.id.trackItemPrice)
        val trackPaymentModeTxtView = view.findViewById<TextView>(R.id.trackPaymentModeTxtView)
        val trackOrderPlacedDateTxtView = view.findViewById<TextView>(R.id.trackOrderPlacedDateTxtView)
        val trackStatusSymbol = view.findViewById<TextView>(R.id.trackStatusSymbol)

        if (shipmentResponse.ShipmentData.Shipment.StatusType == "DL"){
            trackStatusSymbol.visibility = View.VISIBLE
        }else{
            trackStatusSymbol.visibility = View.GONE
        }

        trackingIdTextView.text = order.bd_order_id

        DatabaseConstants.getParticularProductReference(order.product_id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(ProductItem::class.java)
                        if (item != null) {
                            trackOrderId.text = "${order.order_id}"
                            Glide.with(requireContext()).load(item.imageUrl1)
                                .into(trackItemImageView)
                            trackItemProductName.text = item.getProductName()
                            trackItemQuantity.text = "Quantity: ${order.order_quantity}"
                            trackItemPrice.text = order.payable
                            if (order.delivery_charge == "0"){
                                trackDeliveryChargeTextView.text = "Free"
                                trackDeliveryChargeTextView.setTextColor(requireContext().getColor(R.color.green))
                            }
                            else{
                                trackDeliveryChargeTextView.text = order.delivery_charge
                            }

                            trackOrderPlacedDateTxtView.text = order.created_at.substring(0, 10)
                            trackPaymentModeTxtView.text = order.customer_payment_method

                            Log.d("------------------", item.productName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        // here we have to pass scan details for particular order id
        val scanDetailList = shipmentResponse.ShipmentData.Shipment.Scans.ScanDetail

        // Create and set up the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.trackStatusRv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = TrackStatusAdapter(requireContext(), scanDetailList)
        recyclerView.adapter = adapter
    }
}