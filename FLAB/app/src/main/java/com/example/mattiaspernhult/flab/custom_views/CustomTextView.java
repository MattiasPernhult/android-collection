package com.example.mattiaspernhult.flab.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mattiaspernhult on 2015-09-15.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        StringBuilder builder = new StringBuilder();
        builder.append(text);
        if (text.toString().contains("-")) {
            builder.append(", not good...");
        } else {
            builder.append(", good!");
        }
        super.setText(builder, type);
    }
}
