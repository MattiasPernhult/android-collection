package com.example.mattiaspernhult.p2.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.mattiaspernhult.p2.R;
import com.example.mattiaspernhult.p2.interfaces.CustomClickListener;

/**
 * Created by mattiaspernhult on 2015-10-04.
 */
public class NewGroupFragmentDialog extends DialogFragment {

    private CustomClickListener listener;

    public void setListener(CustomClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.new_group, null);

        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText groupname = (EditText) view.findViewById(R.id.group);
                        if (listener != null) {
                            listener.newGroup(true, groupname.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewGroupFragmentDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
