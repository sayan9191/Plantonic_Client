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
import com.example.plantonic.firebaseClasses.CategoryItem;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<CategoryItem> allCategories = new ArrayList<>();
    Context context;

    public CategoryAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_layout, parent, false);
        return new CategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryItem currentItem = allCategories.get(position);
        Glide.with(context).load(currentItem.categoryImage).centerCrop().into(holder.categoriesImageview);
        holder.categoriesTxtView.setText(currentItem.categoryName);
    }


    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoriesImageview;
        TextView categoriesTxtView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriesImageview =  itemView.findViewById(R.id.categoriesImageview);
            categoriesTxtView =  itemView.findViewById(R.id.categoriesTxtView);
        }
    }



    public void updateCategories(ArrayList<CategoryItem> list){
        allCategories.clear();
        allCategories.addAll(list);
        this.notifyDataSetChanged();
    }
}
