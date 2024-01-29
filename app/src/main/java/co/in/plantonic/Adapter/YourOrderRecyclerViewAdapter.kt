package co.`in`.plantonic.Adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import co.`in`.plantonic.R
import co.`in`.plantonic.utils.constants.DatabaseConstants
import com.google.firebase.database.ValueEventListener
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import co.`in`.plantonic.firebaseClasses.ProductItem
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseError
import android.widget.TextView
import co.`in`.plantonic.Adapter.listeners.TrackOrderListener
import co.`in`.plantonic.retrofit.models.order.Data
import java.util.*
import kotlin.collections.HashMap

class YourOrderRecyclerViewAdapter(var context: Context, val listener: TrackOrderListener) :
    RecyclerView.Adapter<YourOrderRecyclerViewAdapter.ViewHolder>() {
    private val allOrdersMap : HashMap<Int, Data> = HashMap()
    var allOrderItems = ArrayList<Data>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val (orderId, _, productId, _, _, _, _, _, _, _, _, quantity, _, _, timeStamp, payable) = allOrderItems[position]
        val currentOrder = allOrderItems[position]
        DatabaseConstants.getParticularProductReference(currentOrder.product_id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(ProductItem::class.java)
                        if (item != null) {
                            holder.orderId.text = "${currentOrder.order_id}"
                            Glide.with(context).load(item.imageUrl1)
                                .into(holder.deliveredItemImageView)
                            holder.deliveredItemProductName.text = item.getProductName()
                            holder.deliveredItemQuantity.text = "Quantity: ${currentOrder.order_quantity}"
                            holder.deliveredItemPrice.text = currentOrder.payable
                            if (currentOrder.delivery_charge == "0"){
                                holder.orderDeliveryChargeTextView.text = "Free"
                                holder.orderDeliveryChargeTextView.setTextColor(context.getColor(R.color.green))
                            }
                            else{
                                holder.orderDeliveryChargeTextView.text = currentOrder.delivery_charge
                            }

                            holder.orderPlacedDateTxtView.text = currentOrder.created_at.substring(0, 10)
                            holder.deliveryPaymentModeTxtView.text = currentOrder.customer_payment_method

                            Log.d("------------------", item.productName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        //track order
        holder.trackOrder.setOnClickListener {
            listener.onTrackOrderClicked(currentOrder)
        }

        // Go to product
        holder.itemView.rootView.setOnClickListener {
            listener.onOrderedProductClicked(currentOrder.product_id)
        }

    }

    override fun getItemCount(): Int {
        return allOrderItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deliveredItemImageView: ImageView
        var deliveredItemProductName: TextView
        var deliveredItemQuantity: TextView
        var deliveredItemPrice: TextView
        var orderPlacedDateTxtView: TextView
        var orderId: TextView
        var trackOrder: TextView
        var deliveredStatusSymbol: TextView
        var orderDeliveryChargeTextView : TextView
        var deliveryPaymentModeTxtView : TextView

        init {
            deliveredItemImageView = itemView.findViewById(R.id.deliveredItemImageView)
            deliveredItemProductName = itemView.findViewById(R.id.deliveredItemProductName)
            deliveredItemQuantity = itemView.findViewById(R.id.deliveredItemQuantity)
            deliveredItemPrice = itemView.findViewById(R.id.deliveredItemPrice)
            orderPlacedDateTxtView = itemView.findViewById(R.id.orderPlacedDateTxtView)
            orderId = itemView.findViewById(R.id.orderId)
            trackOrder = itemView.findViewById(R.id.trackOrder)
            deliveredStatusSymbol = itemView.findViewById(R.id.deliveredStatusSymbol)
            orderDeliveryChargeTextView = itemView.findViewById(R.id.orderDeliveryChargeTextView)
            deliveryPaymentModeTxtView = itemView.findViewById(R.id.deliveryPaymentModeTxtView)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAllOrders(allProducts: List<Data>) {
        allProducts.forEach {
            allOrdersMap[it.order_id] = it
        }
        allOrderItems.clear()
        allOrderItems.addAll(allOrdersMap.values.sortedBy { it.order_id }.reversed())
        notifyDataSetChanged()
    }
}