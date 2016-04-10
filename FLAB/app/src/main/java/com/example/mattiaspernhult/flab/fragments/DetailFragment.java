package com.example.mattiaspernhult.flab.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.models.Economi;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private Economi economi;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        setRetainInstance(true);
        ImageView ivCategory = (ImageView) v.findViewById(R.id.ivCategory);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
        TextView tvPrice = (TextView) v.findViewById(R.id.tvPrice);

        tvTitle.setText(this.economi.getTitle());
        tvDate.setText(this.economi.getDate());
        tvPrice.setText(String.valueOf(this.economi.getPrice()) + " kr");

        String category = economi.getCategory();
        switch (category) {
            case "Food":
                ivCategory.setImageResource(R.drawable.food_rr);
                break;
            case "Travel":
                ivCategory.setImageResource(R.drawable.travel_rr);
                break;
            case "Living":
                ivCategory.setImageResource(R.drawable.living_rr);
                break;
            case "Spare Time":
                ivCategory.setImageResource(R.drawable.spare_time_rr);
                break;
            case "Others":
                ivCategory.setImageResource(R.drawable.other_rr);
                break;
            case "Salary":
                ivCategory.setImageResource(R.drawable.money_rr);
                break;
        }

        return v;
    }


    public void setEconomi(Economi e) {
        this.economi = e;
    }
}
