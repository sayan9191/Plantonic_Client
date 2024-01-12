package com.example.plantonic.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantonic.R;
import com.example.plantonic.ShipmentResponse;

import java.util.List;

public class TrackStatusAdapter extends RecyclerView.Adapter<TrackStatusAdapter.ViewHolder> {
    private final List<ShipmentResponse.ScanDetail> scanDetailList;
    private final Context context;

    public TrackStatusAdapter(Context context, List<ShipmentResponse.ScanDetail> scanDetailList) {
        this.context = context;
        this.scanDetailList = scanDetailList;
    }

    @NonNull
    @Override
    public TrackStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shipment_details, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShipmentResponse.ScanDetail scanDetail = scanDetailList.get(position);
        holder.statusDate.setText("Scan Date: " + scanDetail.getScanDate());
        holder.statusTitle.setText("Scan: " + scanDetail.getScan());
        holder.statusTime.setText("Scan Time: " + scanDetail.getScanTime());
    }

    @Override
    public int getItemCount() {
        return scanDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView statusTitle, statusDate, statusTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTitle = itemView.findViewById(R.id.statusTitle);
            statusDate = itemView.findViewById(R.id.statusDate);
            statusTime = itemView.findViewById(R.id.statusTime);
        }
    }
}
