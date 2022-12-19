package com.example.plantonic.ui.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plantonic.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CartBottomSheet extends BottomSheetDialogFragment {
    public CartBottomSheet(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.proceed_to_items,container,false);
        return view;
    }
}
