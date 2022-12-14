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
import com.example.plantonic.Adapter.listeners.FavouriteListener;
import com.example.plantonic.R;
import com.example.plantonic.ui.cartfav.FavouriteViewModel;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {
    ArrayList<ProductItem> allFavItems = new ArrayList<>();
    private FavouriteViewModel favouriteViewModel;
    Context context;
    FavouriteListener favouriteListener;

    public FavouriteRecyclerViewAdapter(Context context, FavouriteListener favouriteListener, FavouriteViewModel viewModel){
        this.favouriteListener = favouriteListener;
        this.context = context;
        this.favouriteViewModel = viewModel;

    }

    @NonNull
    @Override
    public FavouriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRecyclerViewAdapter.ViewHolder holder, int position) {
        ProductItem productItem = allFavItems.get(position);

        Glide.with(context).load(productItem.imageUrl1).centerCrop().into(holder.productImage);
        holder.productName.setText(productItem.productName);
        holder.productPrice.setText("₹" +productItem.actualPrice+"/-");
        holder.actualPrice.setText("₹" +productItem.listedPrice+"/-");

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteViewModel.removeFromFav(FirebaseAuth.getInstance().getUid(), productItem.getProductId());
            }
        });

        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteViewModel.addToCart(FirebaseAuth.getInstance().getUid(), productItem.getProductId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return allFavItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,productPrice,actualPrice;
        TextView cartBtn, removeBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            actualPrice = itemView.findViewById(R.id.actualPrice);
            cartBtn = itemView.findViewById(R.id.moveToCartBtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);

        }
    }

    public void updateAllFavItems(List<ProductItem> list){
        allFavItems.clear();
        allFavItems.addAll(list);
        this.notifyDataSetChanged();
    }
}
