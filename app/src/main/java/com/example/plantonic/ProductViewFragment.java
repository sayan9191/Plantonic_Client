package com.example.plantonic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ProductViewFragment extends Fragment {
    int productNo = 0;
    TextView integer_number;
    Button decrease,increase;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_view, container, false);
        integer_number = view.findViewById(R.id.integer_number);
        decrease = view.findViewById(R.id.decrease);
        increase = view.findViewById(R.id.increase);


        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseInteger(view);
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseInteger(view);
            }
        });
        return view;
    }
    public void increaseInteger(View view) {
        productNo = productNo + 1;
        display(productNo);

    }

    public void decreaseInteger(View view) {
        productNo = productNo - 1;
        if (productNo>0){
            display(productNo);
        }
        else{
            productNo= 0;
            display((0));
        }
    }
    private void display(int number) {
        integer_number.setText("" + number);
    }
}