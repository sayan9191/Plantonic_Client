package com.example.plantonic.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.plantonic.firebaseClasses.OrderItem
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.plantonic.R
import com.example.plantonic.utils.constants.DatabaseConstants
import com.google.firebase.database.ValueEventListener
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.example.plantonic.firebaseClasses.ProductItem
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseError
import com.example.plantonic.ui.bottomSheet.BottomSheetTrackStatus
import androidx.fragment.app.FragmentActivity
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class YourOrderRecyclerViewAdapter(var context: Context) :
    RecyclerView.Adapter<YourOrderRecyclerViewAdapter.ViewHolder>() {
    var allOrderItems = ArrayList<OrderItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (orderId, _, productId, _, _, _, _, _, _, _, _, quantity, _, _, timeStamp, payable) = allOrderItems[position]
        DatabaseConstants.getParticularProductReference(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(ProductItem::class.java)
                        if (item != null) {
                            holder.orderId.text = R.string.order_id.toString() + orderId
                            Glide.with(context).load(item.imageUrl1)
                                .into(holder.deliveredItemImageView)
                            holder.deliveredItemProductName.text = item.getProductName()
                            holder.deliveredItemQuantity.setText("Quantity: $quantity")
                            holder.deliveredItemPrice.text = "Price: $payable/-"
                            @SuppressLint("SimpleDateFormat") val formatter =
                                SimpleDateFormat("dd/MM/yyyy")
                            val dateString = formatter.format(Date(timeStamp))
                            holder.orderPlacedDateTxtView.text = dateString
                            val c = Calendar.getInstance()
                            try {
                                c.time = Objects.requireNonNull(formatter.parse(dateString))
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                            c.add(Calendar.DATE, 21)
                            val resultDate = Date(c.timeInMillis)
                            holder.deliveryDate.text = formatter.format(resultDate)
                            Log.d("------------------", item.productName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        //track order
        holder.trackOrder.setOnClickListener {
            val bottomSheet = BottomSheetTrackStatus()
            bottomSheet.show((context as FragmentActivity).supportFragmentManager, "")
        }
    }

    override fun getItemCount(): Int {
        return allOrderItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deliveredItemImageView: ImageView
        var deliveredItemProductName: TextView
        var deliveryDate: TextView
        var deliveredItemQuantity: TextView
        var deliveredItemPrice: TextView
        var orderPlacedDateTxtView: TextView
        var orderId: TextView
        var trackOrder: TextView

        init {
            deliveredItemImageView = itemView.findViewById(R.id.deliveredItemImageView)
            deliveredItemProductName = itemView.findViewById(R.id.deliveredItemProductName)
            deliveryDate = itemView.findViewById(R.id.deliveryDate)
            deliveredItemQuantity = itemView.findViewById(R.id.deliveredItemQuantity)
            deliveredItemPrice = itemView.findViewById(R.id.deliveredItemPrice)
            orderPlacedDateTxtView = itemView.findViewById(R.id.orderPlacedDateTxtView)
            orderId = itemView.findViewById(R.id.orderId)
            trackOrder = itemView.findViewById(R.id.trackOrder)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAllOrders(allProducts: List<OrderItem>?) {
        allOrderItems.clear()
        allOrderItems.addAll(allProducts!!)
        notifyDataSetChanged()
    }
}