package co.in.plantonic.Adapter;

import static co.in.plantonic.utils.constants.DatabaseConstants.getSpecificUserCartItemReference;

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
import co.in.plantonic.Adapter.listeners.FavouriteListener;
import co.in.plantonic.R;
import co.in.plantonic.ui.cartfav.FavouriteViewModel;
import co.in.plantonic.firebaseClasses.ProductItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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

        holder.productPrice.setText("₹" +productItem.actualPrice);
        holder.actualPrice.setText("₹" +productItem.listedPrice);
        int realPrice = Integer.parseInt(productItem.listedPrice);

        int price = Integer.parseInt(productItem.actualPrice);
        int discount = (realPrice - price) * 100 / realPrice;
        holder.favProductOffer.setText(discount + "% off");

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteViewModel.removeFromFav(FirebaseAuth.getInstance().getUid(), productItem.getProductId());
            }
        });


        //On Product Clicked
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteListener.onProductClicked(productItem);
            }
        });

        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteListener.onProductClicked(productItem);
            }
        });

        // Check if already added to cart
        getSpecificUserCartItemReference(FirebaseAuth.getInstance().getUid(), productItem.getProductId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()){
                                    holder.cartBtn.setText("Add to Cart");
                                    holder.cartBtn.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow, context.getTheme()));
                                    holder.cartBtn.setTextColor(context.getResources().getColor(R.color.black, context.getTheme()));

                                    holder.alreadyInCartText.setVisibility(View.GONE);

                                    holder.cartBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            favouriteViewModel.addToCart(FirebaseAuth.getInstance().getUid(), productItem.getProductId());
                                        }
                                    });
                                }else {
                                    holder.cartBtn.setText("Go to Cart");
                                    holder.cartBtn.setBackgroundTintList(context.getResources().getColorStateList(R.color.green, context.getTheme()));
                                    holder.cartBtn.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));

                                    holder.alreadyInCartText.setVisibility(View.VISIBLE);

                                    holder.cartBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (productItem.getCurrentStock() > 0){
                                                favouriteListener.onGoToCartBtnClicked(productItem.getProductId());
                                            }else{
                                                Toast.makeText(context, "Sorry, we're out of stock currently.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
//                                favouriteListener.onGoToCartBtnClicked(productItem.getProductId());
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
        TextView alreadyInCartText;
        TextView favProductOffer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            actualPrice = itemView.findViewById(R.id.actualPrice);
            cartBtn = itemView.findViewById(R.id.moveToCartBtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            alreadyInCartText = itemView.findViewById(R.id.alreadyInCartTextView);
            favProductOffer = itemView.findViewById(R.id.favProductOffer);
        }
    }

    public void updateAllFavItems(List<ProductItem> list){
        allFavItems.clear();
        allFavItems.addAll(list);
        this.notifyDataSetChanged();
    }
}
