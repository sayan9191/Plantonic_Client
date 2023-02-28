package com.example.myschoolwearclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myschoolwearclient.Adapter.listeners.OnSearchListener;
import com.example.myschoolwearclient.firebaseClasses.search.SearchProductItem;
import com.example.plantonic.R;

import java.util.ArrayList;
import java.util.List;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {

    ArrayList<SearchProductItem> allSearchItem = new ArrayList<>();
    Context context;
    OnSearchListener listener;

    public SearchResultAdapter(Context context, OnSearchListener listener){
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchProductItem currentItem = allSearchItem.get(position);

        Glide.with(context).load(currentItem.getProductImageUrl()).into(holder.productImage);
        holder.productName.setText(currentItem.getProductName());

        //Handle Clicks
        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnSearchProductClicked(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allSearchItem.size();
    }

    public void updateSearchList(List<SearchProductItem> list){
        allSearchItem.clear();
        allSearchItem.addAll(list);
        this.notifyDataSetChanged();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        ConstraintLayout product;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.searchProductImage);
            productName = itemView.findViewById(R.id.searchProductName);
            product = itemView.findViewById(R.id.searchResultProduct);
        }
    }
}

