package com.example.plantonic.ui.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantonic.Adapter.TrackStatusAdapter;
import com.example.plantonic.R;
import com.example.plantonic.ShipmentResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetTrackStatus extends BottomSheetDialogFragment {
    private ShipmentResponse shipmentResponse;

    public BottomSheetTrackStatus() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_track_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // here we have to pass scan details for particular order id
        List<ShipmentResponse.ScanDetail> scanDetailList = shipmentResponse.getShipmentData().getShipment().getScans().getScanDetailList();

        // Create and set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.trackStatusRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TrackStatusAdapter adapter = new TrackStatusAdapter(getContext(), scanDetailList);
        recyclerView.setAdapter(adapter);

    }
}
