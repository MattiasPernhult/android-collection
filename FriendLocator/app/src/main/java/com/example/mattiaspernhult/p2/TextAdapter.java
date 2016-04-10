package com.example.mattiaspernhult.p2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mattiaspernhult.p2.models.TextChat;

import java.util.List;

/**
 * Created by mattiaspernhult on 2015-10-29.
 */
public class TextAdapter extends ArrayAdapter<TextChat> {

    public TextAdapter(Context context, int resource, List<TextChat> objects) {
        super(context, resource, objects);
    }

   /*
    public TextAdapter(Context context, int resource) {
        super(context, resource);
    }
    */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.text_item, null);
        }

        TextChat textChat = getItem(position);

        Log.d("Message", "pos: " + position + ",  bild: " + textChat.isImage());

        if (textChat != null) {
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvGroup = (TextView) view.findViewById(R.id.tvGroup);
            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            tvName.setText(textChat.getName());
            tvGroup.setText(textChat.getGroup());
            tvMessage.setText(textChat.getMessage());

            if (textChat.isImage()) {
                byte[] image = textChat.getImageBuffer();
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                imageView.getLayoutParams().height = 300;
                imageView.getLayoutParams().width = 300;
            } else {
                imageView.getLayoutParams().height = 0;
                imageView.getLayoutParams().width = 0;
            }

        }

        return view;
    }
}
