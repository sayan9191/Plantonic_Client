package com.example.plantonic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CheckOutOne extends Fragment {
    LinearLayout linearAddNewAddressBtn;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_check_out_one, container, false);
        linearAddNewAddressBtn = view.findViewById(R.id.linearAddNewAddressBtn);



        linearAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true)
                        .addToBackStack("address")
                        .replace(R.id.fragmentContainerView, new AddressGivenFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}