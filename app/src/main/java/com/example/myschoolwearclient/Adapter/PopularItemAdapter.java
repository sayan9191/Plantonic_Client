package com.example.myschoolwearclient.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.myschoolwearclient.Adapter.listeners.OnProductListener;
import com.example.myschoolwearclient.firebaseClasses.ProductItem;
import com.example.plantonic.R;

import java.util.ArrayList;

public class PopularItemAdapter extends RecyclerView.Adapter<PopularItemAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductItem> popularProductItems = new ArrayList<>();
    OnProductListener listener;

    public PopularItemAdapter(Context context, OnProductListener listener){
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public PopularItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);

        return new PopularItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem productItem = popularProductItems.get(position);
        Glide.with(context).load(productItem.imageUrl1).centerCrop().into(holder.productImage);
        holder.productName.setText(productItem.productName);
        holder.productPrice.setText("₹" + productItem.listedPrice+ "/-");
        holder.actualPrice.setText("₹"+ productItem.actualPrice+ "/-");

        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onProductClick(productItem);
            }
        });
    }


    @Override
    public int getItemCount() {
        return popularProductItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,productPrice,actualPrice;
        RelativeLayout product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.actualPrice);
            actualPrice = itemView.findViewById(R.id.productPrice);
            product = itemView.findViewById(R.id.productItem);
        }
    }

    public void updatePopularProducts(ArrayList<ProductItem> list){
        popularProductItems.clear();
        popularProductItems.addAll(list);
        this.notifyDataSetChanged();
    }
}