package com.example.myschoolwearclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myschoolwearclient.Adapter.listeners.CartListner;
import com.example.myschoolwearclient.firebaseClasses.CartItem;
import com.example.myschoolwearclient.firebaseClasses.ProductItem;
import com.example.myschoolwearclient.ui.cartfav.CartViewModel;
import com.example.plantonic.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    ArrayList<ProductItem> allCartProductItems = new ArrayList<>();
    ArrayList<CartItem> allCartItems = new ArrayList<>();
    private CartViewModel cartViewModel;
    CartListner cartListner;
    Context context;
    Long totalPayable;

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

        int realPrice = Integer.parseInt(productItem.listedPrice);
        int price = Integer.parseInt(productItem.actualPrice);
        int discount = (realPrice - price) * 100 / realPrice;
        holder.cartProductOffer.setText(discount + "% off");

        if (totalPayable >= 500L){
            holder.deliveryPrice.setText("FREE");
        }else{
            holder.deliveryPrice.setText("₹" + productItem.getDeliveryCharge() + "/-");
        }

        // On Cart Item Clicked
        holder.cartProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartListner.onCartItemClicked(productItem);
            }
        });

        holder.cartProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartListner.onCartItemClicked(productItem);
            }
        });



        //remove button from cart item
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartListner.onRemoveFromCartClicked(productItem);
            }
        });

        //Increase product Items
        holder.cartIncreaseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartViewModel.addIncreaseCartQuantity(FirebaseAuth.getInstance().getUid(), productItem.getProductId(), Long.parseLong(holder.cartProductItemNo.getText().toString()));
            }
        });

        // decrease product Items
        holder.cartDecreaseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long quantity = Long.parseLong(holder.cartProductItemNo.getText().toString());
                if (quantity>1){
                    cartViewModel.removeDecreaseCartQuantity(FirebaseAuth.getInstance().getUid(), productItem.getProductId(), quantity);
                }
                else {
                    Toast.makeText(context,"Quantity can't be zero",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (allCartItems.size() == allCartProductItems.size()){
            return allCartItems.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartProductImage,removeBtn;
        TextView cartProductName, cartProductPrice, cartActualPrice, cartProductOffer;
        TextView cartDecreaseProduct, cartProductItemNo,cartIncreaseProduct;
        TextView deliveryPrice;

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
            deliveryPrice = itemView.findViewById(R.id.productDeliveryAmount);
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

    public void updateTotalPayable(Long totalPayable){
        this.totalPayable = totalPayable;
        this.notifyDataSetChanged();
    }
}
