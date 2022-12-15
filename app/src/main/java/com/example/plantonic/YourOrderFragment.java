package com.example.plantonic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.plantonic.ui.homeFragment.HomeFragment;
import com.example.plantonic.ui.others.FeedbackFragment;

public class YourOrderFragment extends Fragment {
    View view;
    Button btnBack;
    TextView backToHome;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_your_order, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        backToHome = view.findViewById(R.id.backToHome);

        // home back button
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("BackToHome")
                        .replace(R.id.fragmentContainerView, new HomeFragment())
                        .commit();
            }
        });
        // back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack("ReturnHome")
                        .replace(R.id.fragmentContainerView, new HomeFragment())
                        .commit();
            }
        });

        return view;
    }
}