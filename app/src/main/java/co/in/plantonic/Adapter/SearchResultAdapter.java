package co.in.plantonic.Adapter;

import static co.in.plantonic.utils.constants.DatabaseConstants.getParticularProductReference;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import co.in.plantonic.Adapter.listeners.OnSearchListener;
import co.in.plantonic.R;
import co.in.plantonic.firebaseClasses.ProductItem;
import co.in.plantonic.firebaseClasses.search.SearchProductItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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

        holder.productPrice.setVisibility(View.INVISIBLE);
        holder.actualPrice.setVisibility(View.INVISIBLE);
        holder.discount.setVisibility(View.INVISIBLE);
        holder.searchPriceLoaderProgressBar.setVisibility(View.VISIBLE);

        // Load product details
        getParticularProductReference(currentItem.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItem item = snapshot.getValue(ProductItem.class);
                    if (item != null){

                        holder.searchPriceLoaderProgressBar.setVisibility(View.GONE);
                        holder.productPrice.setVisibility(View.VISIBLE);
                        holder.actualPrice.setVisibility(View.VISIBLE);
                        holder.discount.setVisibility(View.VISIBLE);

                        holder.productPrice.setText("₹" + item.listedPrice);
                        holder.actualPrice.setText("₹"+ item.actualPrice);

                        int realPrice = Integer.parseInt(item.listedPrice);
                        int price = Integer.parseInt(item.actualPrice);
                        int discount = (realPrice - price) * 100 / realPrice;
                        holder.discount.setText(discount + "% off");

                        Log.d("------------------", item.productName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        TextView productName, actualPrice, productPrice, discount;
        ConstraintLayout product;
        ProgressBar searchPriceLoaderProgressBar;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.searchProductImage);
            productName = itemView.findViewById(R.id.searchProductName);
            product = itemView.findViewById(R.id.searchResultProduct);
            actualPrice = itemView.findViewById(R.id.searchActualPrice);
            productPrice = itemView.findViewById(R.id.searchProductPrice);
            discount = itemView.findViewById(R.id.searchItemDiscountPercent);
            searchPriceLoaderProgressBar = itemView.findViewById(R.id.searchPriceLoaderProgressBar);
        }
    }
}

