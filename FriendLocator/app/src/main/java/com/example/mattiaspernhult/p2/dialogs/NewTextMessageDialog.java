package com.example.mattiaspernhult.p2.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mattiaspernhult.p2.R;
import com.example.mattiaspernhult.p2.interfaces.CustomClickListener;

/**
 * Created by mattiaspernhult on 2015-10-29.
 */
public class NewTextMessageDialog extends DialogFragment {

    private CustomClickListener listener;
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setListener(CustomClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.new_message, null);

        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText textMessage = (EditText) view.findViewById(R.id.message);
                        if (textMessage.getText().toString().length() >= 5) {
                            if (listener != null) {
                                boolean save;
                                if (type == 1) {
                                    save = true;
                                } else {
                                    save = false;
                                }
                                listener.newGroup(save, textMessage.getText().toString());
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.length_of_text, Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewTextMessageDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
