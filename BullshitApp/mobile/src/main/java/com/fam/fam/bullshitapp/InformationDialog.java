package com.fam.fam.bullshitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class InformationDialog extends DialogFragment {

    private ArrayList<String> names;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        names = args.getStringArrayList("names");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.information, null);
        TextView tvPersons = (TextView) view.findViewById(R.id.tvPersons);

        if (names == null) {
            tvPersons.setText(R.string.no_persons_on_ISS);
        } else {
            String text = "";
            for (int i = 0; i < names.size(); i++) {
                text += names.get(i);
                if (i != names.size() - 1) {
                    text += "\n";
                }
            }
            tvPersons.setText(text);
        }

        builder.setView(view)
                .setNeutralButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InformationDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
