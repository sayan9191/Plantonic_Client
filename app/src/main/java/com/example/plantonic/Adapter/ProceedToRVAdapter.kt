package com.example.plantonic.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantonic.R
import com.example.plantonic.firebaseClasses.CartItem
import com.example.plantonic.firebaseClasses.ProductItem

class ProceedToRVAdapter(private val context: Context) : RecyclerView.Adapter<ProceedToRVAdapter.ProceedToViewHolder>() {

    var allCartProductItems = ArrayList<ProductItem>()
    var allCartItems = ArrayList<CartItem>()

    inner class ProceedToViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val summaryProductImage : ImageView = itemView.findViewById(R.id.summaryOrderProductImage)
        val summaryProductName : TextView = itemView.findViewById(R.id.summaryOrderProductName)
        val summaryProductPrice : TextView = itemView.findViewById(R.id.summaryOrderProductPrice)
        val summaryActualPrice : TextView = itemView.findViewById(R.id.summaryOrderActualPrice)
        val summaryProductOffer : TextView = itemView.findViewById(R.id.summaryOrderProductOffer)
        val summaryProductQuantity : TextView = itemView.findViewById(R.id.summaryOrderQuantity)
        val deliveryCharge : TextView = itemView.findViewById(R.id.summaryProductDeliveryAmount)
        val deliveryChargeLabel : TextView = itemView.findViewById(R.id.deliveryChargeLabelTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProceedToViewHolder {
        return ProceedToViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_summary_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProceedToViewHolder, position: Int) {
        holder.deliveryCharge.visibility = View.GONE
//        holder.summaryProductOffer.visibility = View.GONE
        holder.deliveryChargeLabel.visibility = View.GONE

        val currentProductItem = allCartProductItems[position]
        val currentCartItem = allCartItems[position]

        Glide.with(context).load(currentProductItem.imageUrl1).into(holder.summaryProductImage)


        holder.summaryProductName.text = currentProductItem.productName


        val realPrice: Int = currentProductItem.listedPrice.toInt()
        val price: Int = currentProductItem.actualPrice.toInt()
        val discount = (realPrice - price) * 100 / realPrice
        holder.summaryProductOffer.text = "$discount% off"


        holder.summaryProductQuantity.text = currentCartItem.quantity.toString()
        holder.summaryProductPrice.text = "₹${currentProductItem.listedPrice}"
        holder.summaryActualPrice.text = "₹${currentProductItem.actualPrice}"
    }

    override fun getItemCount(): Int {
        return if (allCartItems.size == allCartProductItems.size) {
            allCartItems.size
        } else {
            0
        }
    }

    fun updateAllCartProductItems(list: List<ProductItem>) {
        allCartProductItems.clear()
        allCartProductItems.addAll(list)
        notifyDataSetChanged()
    }

    fun updateAllCartItems(list: List<CartItem>) {
        allCartItems.clear()
        allCartItems.addAll(list)
        notifyDataSetChanged()
    }
}