package co.`in`.plantonic.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import co.`in`.plantonic.Adapter.listeners.FavouriteListener
import co.`in`.plantonic.R
import co.`in`.plantonic.firebaseClasses.CartItem
import co.`in`.plantonic.firebaseClasses.ProductItem
import co.`in`.plantonic.ui.cartfav.FavouriteViewModel
import co.`in`.plantonic.utils.constants.DatabaseConstants
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class FavouriteRecyclerViewAdapter(
    context: Context,
    favouriteListener: FavouriteListener,
    viewModel: FavouriteViewModel
) : RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder?>() {
    var allFavItems: ArrayList<ProductItem> = ArrayList<ProductItem>()
    private val favouriteViewModel: FavouriteViewModel
    var context: Context
    var favouriteListener: FavouriteListener

    init {
        this.favouriteListener = favouriteListener
        this.context = context
        favouriteViewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem: ProductItem = allFavItems[holder.adapterPosition]
        Glide.with(context).load(productItem.imageUrl1).centerCrop().into(holder.productImage)
        holder.productName.text = productItem.productName
        holder.productPrice.text = "₹" + productItem.actualPrice
        holder.actualPrice.text = "₹" + productItem.listedPrice
        val realPrice: Int = productItem.listedPrice.toInt()
        val price: Int = productItem.actualPrice.toInt()
        val discount = (realPrice - price) * 100 / realPrice
        holder.favProductOffer.text = "$discount% off"
        holder.removeBtn.setOnClickListener(View.OnClickListener {
            favouriteViewModel.removeFromFav(
                FirebaseAuth.getInstance().uid,
                productItem.getProductId()
            )
        })


        //On Product Clicked
        holder.productImage.setOnClickListener { favouriteListener.onProductClicked(productItem) }
        holder.productName.setOnClickListener(View.OnClickListener {
            favouriteListener.onProductClicked(
                productItem
            )
        })

        // Check if already added to cart
        DatabaseConstants.getSpecificUserCartItemReference(
            FirebaseAuth.getInstance().uid,
            productItem.getProductId()
        )
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.getValue(CartItem::class.java)?.let {
                            if (productItem.productId == it.productId){
                                holder.cartBtn.text = "Go to Cart"
                                holder.cartBtn.backgroundTintList = context.resources.getColorStateList(
                                    R.color.green,
                                    context.theme
                                )
                                holder.cartBtn.setTextColor(
                                    context.resources.getColor(
                                        R.color.white,
                                        context.theme
                                    )
                                )
                                holder.alreadyInCartText.visibility = View.VISIBLE
                                holder.cartBtn.setOnClickListener(View.OnClickListener {
                                    if (productItem.currentStock > 0) {
                                        favouriteListener.onGoToCartBtnClicked(productItem.getProductId())
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Sorry, we're out of stock currently.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            } else{
                                holder.cartBtn.text = "Add to Cart"
                                holder.cartBtn.backgroundTintList = context.resources.getColorStateList(
                                    R.color.yellow,
                                    context.theme
                                )
                                holder.cartBtn.setTextColor(
                                    context.resources.getColor(
                                        R.color.black,
                                        context.theme
                                    )
                                )
                                holder.alreadyInCartText.visibility = View.GONE
                                holder.cartBtn.setOnClickListener(View.OnClickListener {
                                    favouriteListener.onAddToCartClicked(productItem, holder.adapterPosition)
                                })
                            }
                        }
                    } else {
                        holder.cartBtn.text = "Add to Cart"
                        holder.cartBtn.backgroundTintList = context.resources.getColorStateList(
                            R.color.yellow,
                            context.theme
                        )
                        holder.cartBtn.setTextColor(
                            context.resources.getColor(
                                R.color.black,
                                context.theme
                            )
                        )
                        holder.alreadyInCartText.visibility = View.GONE
                        holder.cartBtn.setOnClickListener(View.OnClickListener {
                            favouriteListener.onAddToCartClicked(productItem, holder.adapterPosition)
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
//                                favouriteListener.onGoToCartBtnClicked(productItem.getProductId());
                }
            })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImage: ImageView
        var productName: TextView
        var productPrice: TextView
        var actualPrice: TextView
        var cartBtn: TextView
        var removeBtn: TextView
        var alreadyInCartText: TextView
        var favProductOffer: TextView

        init {
            productImage = itemView.findViewById<ImageView>(R.id.productImage)
            productName = itemView.findViewById<TextView>(R.id.productName)
            productPrice = itemView.findViewById<TextView>(R.id.productPrice)
            actualPrice = itemView.findViewById<TextView>(R.id.actualPrice)
            cartBtn = itemView.findViewById<TextView>(R.id.moveToCartBtn)
            removeBtn = itemView.findViewById<TextView>(R.id.removeBtn)
            alreadyInCartText = itemView.findViewById<TextView>(R.id.alreadyInCartTextView)
            favProductOffer = itemView.findViewById<TextView>(R.id.favProductOffer)
        }
    }

    fun updateAllFavItems(list: List<ProductItem>?) {
        allFavItems.clear()
        allFavItems.addAll(list!!)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return allFavItems.size
    }
}