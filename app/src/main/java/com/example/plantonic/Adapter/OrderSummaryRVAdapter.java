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
import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.CartItem;
import com.example.plantonic.firebaseClasses.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryRVAdapter extends RecyclerView.Adapter<OrderSummaryRVAdapter.ViewHolder> {

    ArrayList<ProductItem> allCartProductItems = new ArrayList<>();
    ArrayList<CartItem> allCartItems = new ArrayList<>();
    Context context;
    Long totalPayable;

    public OrderSummaryRVAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem productItem = allCartProductItems.get(position);
        CartItem cartItem = allCartItems.get(position);

        Glide.with(context).load(productItem.imageUrl1).centerCrop().into(holder.summaryProductImage);

        if (productItem.productName.length() > 60){
            holder.summaryProductName.setText(productItem.productName.substring(0, 60) + "...");
        }else{
            holder.summaryProductName.setText(productItem.productName);
        }
        holder.summaryProductPrice.setText("₹" +productItem.actualPrice);
        holder.summaryActualPrice.setText("₹" +productItem.listedPrice);
        holder.summaryProductQuantity.setText(cartItem.getQuantity().toString());

        int realPrice = Integer.parseInt(productItem.listedPrice);
        int price = Integer.parseInt(productItem.actualPrice);
        int discount = (realPrice - price) * 100 / realPrice;
        holder.summaryProductOffer.setText(discount + "% off");

        if (totalPayable >= 500L){
            holder.deliveryCharge.setText("FREE");
        }else{
            holder.deliveryCharge.setText("₹" + productItem.getDeliveryCharge());
        }
    }

    @Override
    public int getItemCount() {
        if (allCartItems.size() == allCartProductItems.size()){
            return allCartItems.size();
        }else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView summaryProductImage;
        TextView summaryProductName, summaryProductPrice, summaryActualPrice, summaryProductOffer;
        TextView summaryProductQuantity, deliveryCharge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            summaryProductImage = itemView.findViewById(R.id.summaryOrderProductImage);
            summaryProductName = itemView.findViewById(R.id.summaryOrderProductName);
            summaryProductPrice = itemView.findViewById(R.id.summaryOrderProductPrice);
            summaryActualPrice = itemView.findViewById(R.id.summaryOrderActualPrice);
            summaryProductOffer = itemView.findViewById(R.id.summaryOrderProductOffer);
            summaryProductQuantity = itemView.findViewById(R.id.summaryOrderQuantity);
            deliveryCharge = itemView.findViewById(R.id.summaryProductDeliveryAmount);

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

    public void updatePayable(Long totalPayable){
        this.totalPayable = totalPayable;
        this.notifyDataSetChanged();
    }
}
