package com.example.mattiaspernhult.flab.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mattiaspernhult.flab.C;
import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.controllers.DetailController;
import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.models.Economi;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewInputFragment extends Fragment implements View.OnClickListener {

    private EditText etTitle;
    private EditText etAmount;
    private EditText etDate;
    private Spinner spinnerCategory;
    private Button btnSave;
    private DatePickerDialog datePickerDialog;
    private String[] categoriesE = {"Food", "Travel", "Living", "Spare Time", "Others"};
    private String[] categoriesI = {"Salary", "Others"};
    private DetailController detailController;
    private int choice = -1;
    private String amount;
    private String title;
    private String dates;

    private SimpleDateFormat dateFormatter;

    public void setValues(String title, String amount, String d) {
        this.title = title;
        this.amount = amount;
        this.dates = d;
    }

    public String getEtTitle() {
        return etTitle.getText().toString();
    }

    public String getEtAmount() {
        return etAmount.getText().toString();
    }

    public String getDates() {
        return etDate.getText().toString();
    }

    public NewInputFragment() {
        // Required empty public constructor
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    private void setDateTimeField() {
        etDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_input, container, false);
        etTitle = (EditText) v.findViewById(R.id.etTitle);
        etAmount = (EditText) v.findViewById(R.id.etAmount);
        etDate = (EditText) v.findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.requestFocus();

        spinnerCategory = (Spinner) v.findViewById(R.id.spinner);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        setDateTimeField();

        if (choice == 0) {
            spinnerCategory.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, categoriesI));
        } else if (choice == 1) {
            spinnerCategory.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, categoriesE));
        }

        btnSave.setOnClickListener(new ButtonListener());

        if (title != null) {
            etTitle.setText(title);
        }
        if (amount != null) {
            etAmount.setText(amount);
        }

        if (dates != null) {
            etDate.setText(dates);
        }

        return v;
    }

    public void setController(DetailController controller) {
        this.detailController = controller;
    }

    @Override
    public void onClick(View v) {
        datePickerDialog.show();
    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String date = etDate.getText().toString().trim();

            String title = etTitle.getText().toString().trim();
            int amount = Integer.valueOf(etAmount.getText().toString().trim());
            String category = spinnerCategory.getSelectedItem().toString().trim();
            Economi e = new Economi(date, title, category, amount);
            detailController.add(e);
            getActivity().finish();
            if (choice == 0) {
                Toast.makeText(getActivity(), SharedPreManager.getUser(getActivity()) + ", the new income has been added", Toast.LENGTH_LONG).show();
            } else if (choice == 1) {
                Toast.makeText(getActivity(), SharedPreManager.getUser(getActivity()) + ", the new expense has been added", Toast.LENGTH_LONG).show();
            }
        }
    }
}
