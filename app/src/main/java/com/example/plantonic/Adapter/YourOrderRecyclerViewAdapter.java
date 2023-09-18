package com.example.plantonic.Adapter;

import static com.example.plantonic.utils.constants.DatabaseConstants.getParticularProductReference;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plantonic.R;
import com.example.plantonic.firebaseClasses.OrderItem;
import com.example.plantonic.firebaseClasses.ProductItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class YourOrderRecyclerViewAdapter extends RecyclerView.Adapter<YourOrderRecyclerViewAdapter.ViewHolder> {
    ArrayList<OrderItem> allOrderItems = new ArrayList<>();
    Context context;

    public YourOrderRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public YourOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item_layout,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourOrderRecyclerViewAdapter.ViewHolder holder, int position) {
        OrderItem currentItem = allOrderItems.get(position);

        getParticularProductReference(currentItem.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItem item = snapshot.getValue(ProductItem.class);
                    if (item != null){
                        Glide.with(context).load(item.imageUrl1).into(holder.deliveredItemImageView);
                        holder.deliveredItemProductName.setText(item.getProductName());

                        holder.deliveredItemDescription.setText(item.getProductDescription());




                        holder.deliveredItemPrice.setText("Price: "+currentItem.getPayable()+"/-");
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = formatter.format(new Date(currentItem.getTimeStamp()));

                        holder.orderPlacedDateTxtView.setText(dateString);


                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(formatter.parse(dateString));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 21);

                        Date resultDate = new Date(c.getTimeInMillis());

                        holder.deliveryDate.setText(formatter.format(resultDate));

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
        return allOrderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deliveredItemImageView;
        TextView deliveredItemProductName, deliveryDate,deliveredItemDescription, deliveredItemPrice,orderPlacedDateTxtView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deliveredItemImageView = itemView.findViewById(R.id.deliveredItemImageView);
            deliveredItemProductName = itemView.findViewById(R.id.deliveredItemProductName);
            deliveryDate = itemView.findViewById(R.id.deliveryDate);
            deliveredItemDescription = itemView.findViewById(R.id.deliveredItemDescription);
            deliveredItemPrice = itemView.findViewById(R.id.deliveredItemPrice);
            orderPlacedDateTxtView = itemView.findViewById(R.id.orderPlacedDateTxtView);
        }
    }

    public void updateAllOrders(List<OrderItem> allProducts){
        allOrderItems.clear();
        allOrderItems.addAll(allProducts);
        this.notifyDataSetChanged();
    }
}
