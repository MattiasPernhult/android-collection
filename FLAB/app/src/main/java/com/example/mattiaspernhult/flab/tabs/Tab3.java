package com.example.mattiaspernhult.flab.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.controllers.MainController;
import com.github.mikephil.charting.charts.PieChart;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3 extends Fragment{

    private MainController controller;
    private PieChart chart;

    public Tab3() {
        // Required empty public constructor
    }

    public void setController(MainController mainController) {
        this.controller = mainController;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_3, container, false);

        chart = (PieChart) v.findViewById(R.id.pie_chart);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
        chart.setHoleRadius(7);
        chart.setTransparentCircleRadius(10);

        chart.setRotationEnabled(false);

        updateDiagram(null);

        return v;
    }

    public void updateDiagram(String sinceDate) {
        chart.setData(controller.getDataForCategory(sinceDate));
        chart.highlightValues(null);
        chart.invalidate();
    }
}
