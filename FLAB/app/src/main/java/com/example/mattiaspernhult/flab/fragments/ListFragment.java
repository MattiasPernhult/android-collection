package com.example.mattiaspernhult.flab.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mattiaspernhult.flab.C;
import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.controllers.StartController;
import com.example.mattiaspernhult.flab.models.User;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private StartController controller;

    public ListFragment() {
        // Required empty public constructor
    }

    public void setController(StartController controller) {
        this.controller = controller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listUser = (ListView) v.findViewById(R.id.listUser);

        List<String> usersList = C.getUserList(getActivity());

        listUser.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, usersList));

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                controller.setUser(position);
                controller.changeActivity();
            }
        });

        return v;
    }
}
