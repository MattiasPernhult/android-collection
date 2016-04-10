package com.example.mattiaspernhult.flab.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.connections.Dba;
import com.example.mattiaspernhult.flab.controllers.MainController;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab5 extends Fragment {

    private TextView tvTotalIncome;
    private TextView tvTotalExpense;
    private TextView tvTotal;
    private MainController controller
            ;

    public Tab5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_5, container, false);

        tvTotalIncome = (TextView) v.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = (TextView) v.findViewById(R.id.tvTotalExpense);
        tvTotal = (TextView) v.findViewById(R.id.tvTotal);

        Dba dba = new Dba(getActivity());
        int totalIncome = dba.getTotalIncome(controller.getDate());
        int totalExpense = dba.getTotalExpense(controller.getDate());
        int total = totalIncome - totalExpense;
        setDataForTextView(String.valueOf(totalIncome), String.valueOf(totalExpense), String.valueOf(total));

        return v;
    }

    public void setDataForTextView(String ti, String te, String t) {
        tvTotalIncome.setText("\t" + ti);
        tvTotalExpense.setText("\t" + te);
        tvTotal.setText("\t" + t);
    }


    public void setController(MainController mainController) {
        this.controller = mainController;
    }
}
