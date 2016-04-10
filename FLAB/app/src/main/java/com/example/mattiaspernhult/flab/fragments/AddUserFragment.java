package com.example.mattiaspernhult.flab.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.controllers.StartController;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {

    private StartController controller;
    private EditText etFirst;
    private EditText etLast;
    private String first;
    private String last;

    public void setStrings(String f, String l) {
        this.first = f;
        this.last = l;
    }

    public AddUserFragment() {
        // Required empty public constructor
    }

    public void setController(StartController controller) {
        this.controller = controller;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_user, container, false);

        etFirst = (EditText) v.findViewById(R.id.etFirstName);
        etLast = (EditText) v.findViewById(R.id.etLastName);
        Button btnSave = (Button) v.findViewById(R.id.btnSaveUser);

        if (first != null) {
            etFirst.setText(first);
        }

        if (last != null) {
            etLast.setText(last);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = etFirst.getText().toString();
                String lastName = etLast.getText().toString();
                addUser(firstName, lastName);
            }
        });

        return v;
    }

    public String getEtFirstText() {
        return etFirst.getText().toString();
    }

    public String getEtLastText() {
        return etLast.getText().toString();
    }

    private void addUser(String firstName, String lastName) {
        this.controller.addUser(firstName, lastName);
        Toast.makeText(getActivity(), "User " + firstName + " " + lastName + " is added", Toast.LENGTH_SHORT).show();
    }
}
