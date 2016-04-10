package com.fam.fam.bullshitapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;

public class YodaActivity extends Activity {

    private Controller controller;
    private GifImageView gifImageView;
    private TextView textView;
    private Button btnNewAdvice;
    private byte[] giphyImageOnYoda;
    private String yodaText;
    private LinearLayout container;
    private Animation rotation;
    private ImageView imYodaHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoda);

        Intent intent = getIntent();
        controller = (Controller) intent.getSerializableExtra("controller");

        gifImageView = (GifImageView) findViewById(R.id.gifImageViewYoda);
        textView = (TextView) findViewById(R.id.tvYodaText);
        btnNewAdvice = (Button) findViewById(R.id.btnNewAdvice);
        container = (LinearLayout) findViewById(R.id.containerYoda);
        imYodaHead = (ImageView) findViewById(R.id.imYodaHead);
        imYodaHead.setVisibility(View.INVISIBLE);

        rotation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(Animation.INFINITE);
        rotation.setDuration(900);

        if (controller != null) {
            new MyTask().execute(Constants.GET_YODA);
        }

        setButtonListener();
    }

    private void setButtonListener() {
        btnNewAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute(Constants.GET_YODA);
            }
        });
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            gifImageView.stopAnimation();
            container.setAlpha(0.2f);
            imYodaHead.setVisibility(View.VISIBLE);
            imYodaHead.setAnimation(rotation);
        }

        @Override
        protected String doInBackground(String... strings) {
            Thread giphy = new Thread(new Worker(Constants.GET_GIPHY));
            Thread yoda = new Thread(new Worker(Constants.GET_YODA));
            giphy.start();
            yoda.start();
            try {
                giphy.join();
                yoda.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String yodaAdvice) {
            imYodaHead.clearAnimation();
            imYodaHead.setVisibility(View.INVISIBLE);
            container.setAlpha(1.0f);
            gifImageView.setBytes(giphyImageOnYoda);
            gifImageView.startAnimation();
            textView.setText(yodaText);
        }
    }

    private class Worker implements Runnable {

        private String choice;

        public Worker(String choice) {
            this.choice = choice;
        }

        private byte[] getByteArrayFromUrl(String url) throws Exception {
            if (url == null) {
                return null;
            }
            return ByteArrayHttpClient.get(url);
        }

        @Override
        public void run() {
            switch (choice) {
                case Constants.GET_GIPHY:
                    try {
                        giphyImageOnYoda = getByteArrayFromUrl(controller.getYodaGiphy());
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case Constants.GET_YODA:
                    yodaText = controller.getYodaText();
                    break;
            }
        }
    }
}
