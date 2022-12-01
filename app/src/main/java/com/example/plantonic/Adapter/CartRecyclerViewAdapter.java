package com.example.plantonic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plantonic.Adapter.listeners.CartListner;
import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.example.plantonic.ui.cartfav.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    ArrayList<ProductItem> allCartProductItems = new ArrayList<>();
    ArrayList<CartItem> allCartItems = new ArrayList<>();
    private CartViewModel cartViewModel;
    CartListner cartListner;
    Context context;

    public CartRecyclerViewAdapter(Context context,CartListner cartListner, CartViewModel cartViewModel ){
        this.cartListner = cartListner;
        this.cartViewModel = cartViewModel;
        this.context = context;
    }
    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {
        ProductItem productItem = allCartProductItems.get(position);
        CartItem cartItem = allCartItems.get(position);

        Glide.with(context).load(productItem.imageUrl1).centerCrop().into(holder.cartProductImage);
        holder.cartProductName.setText(productItem.productName);
        holder.cartProductPrice.setText("₹" +productItem.actualPrice+"/-");
        holder.cartActualPrice.setText("₹" +productItem.listedPrice+"/-");
        holder.cartProductItemNo.setText(cartItem.getQuantity().toString());

        //remove button from cart item
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartListner.onRemoveFromCartClicked(productItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCartProductItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartProductImage,removeBtn;
        TextView cartProductName, cartProductPrice, cartActualPrice, cartProductOffer;
        TextView cartDecreaseProduct, cartProductItemNo,cartIncreaseProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
            cartActualPrice = itemView.findViewById(R.id.cartActualPrice);
            cartProductOffer = itemView.findViewById(R.id.cartProductOffer);
            cartDecreaseProduct = itemView.findViewById(R.id.cartDecreaseProduct);
            cartProductItemNo = itemView.findViewById(R.id.cartProductItemNo);
            cartIncreaseProduct = itemView.findViewById(R.id.cartIncreaseProduct);
        }
    }
    public void updateAllCartProductItems(List<ProductItem> list){
        allCartProductItems.clear();
        allCartProductItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateAllCartItems(List<CartItem> list){
        allCartItems.clear();
        allCartItems.addAll(list);
        this.notifyDataSetChanged();
    }
}
