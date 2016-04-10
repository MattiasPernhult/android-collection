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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mattiaspernhult.flab.C;
import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.controllers.DetailController;
import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.models.Economi;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScanInputFragment extends Fragment implements View.OnClickListener {

    private EditText etTitle;
    private EditText etAmount;
    private EditText etDate;
    private DatePickerDialog datePickerDialog;
    private Spinner spinnerCategory;
    private Button btnSave;

    private String scanContent;
    private String scanFormat;
    private String amount;
    private String title;
    private String category;
    private boolean notAddReciept;
    private String dates;
    private DetailController controller;
    private String[] categoriesE = {"Food", "Travel", "Living", "Spare Time", "Others"};

    private SimpleDateFormat dateFormatter;


    public ScanInputFragment() {
        // Required empty public constructor
    }

    public void setController(DetailController controller) {
        this.controller = controller;
    }

    public void setValues(String content, String format, String title, String amount, String d) {
        this.scanContent = content;
        this.scanFormat = format;
        this.title = title;
        this.amount = amount;
        this.dates = d;
    }

    public void setValuesWithCate(String content, String format, String title, String amount, String category) {
        this.scanContent = content;
        this.scanFormat = format;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.notAddReciept = true;
    }

    public String getDates() {
        return etDate.getText().toString();
    }

    public String getEtTitle() {
        return etTitle.getText().toString();
    }

    public String getEtAmount() {
        return etAmount.getText().toString();
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
        View v = inflater.inflate(R.layout.fragment_scan_input, container, false);

        etTitle = (EditText) v.findViewById(R.id.etTitle);
        etAmount = (EditText) v.findViewById(R.id.etAmount);
        etDate = (EditText) v.findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.requestFocus();

        spinnerCategory = (Spinner) v.findViewById(R.id.spinner);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        spinnerCategory.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, categoriesE));

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        setDateTimeField();

        btnSave.setOnClickListener(new ButtonListener());

        if (title != null) {
            etTitle.setText(title);
        }
        if (amount != null) {
            etAmount.setText(amount);
        }
        if (category != null) {
            int position = 0;
            for (int i = 0; i < categoriesE.length; i++) {
                if (category.equals(categoriesE[i])) {
                    position = i;
                    break;
                }
            }
            spinnerCategory.setSelection(position);
        }

        if (dates != null) {
            etDate.setText(dates);
        }


        return v;
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
            controller.add(e);
            if (!notAddReciept) {
                controller.addReceipt(e);
            }
            getActivity().finish();
            Toast.makeText(getActivity(), SharedPreManager.getUser(getActivity()) + ", the new expense has been added", Toast.LENGTH_LONG).show();
        }
    }
}
