package com.example.myschoolwearclient.ui.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plantonic.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetPP extends BottomSheetDialogFragment {
    public BottomSheetPP() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.privatepolicy_xml,container,false);
        return view;
    }
}
