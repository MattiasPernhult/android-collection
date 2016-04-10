package com.example.mattiaspernhult.flab.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.RecyclerItemClickListener;
import com.example.mattiaspernhult.flab.connections.RecyclerViewAdapter;
import com.example.mattiaspernhult.flab.controllers.MainController;
import com.example.mattiaspernhult.flab.models.Economi;

import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-12.
 */
public class Tab2 extends Fragment {

    private MainController mainController;
    private Tab2.RecyclerClick listener;

    public Tab2() {

    }

    public void setListener(Tab2.RecyclerClick listener) {
        this.listener = listener;
    }

    public void setController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewTab2);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        List<Economi> economiList = mainController.getExpenses(null);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(economiList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Economi> e = mainController.getExpenses(null);
                listener.onClick(e.get(position), 1);
            }
        }));

        mainController.setAdapter(adapter, 2);

        return v;
    }

    public interface RecyclerClick {
        void onClick(Economi e, int tab);
    }
}
