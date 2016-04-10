package com.fam.fam.bullshitapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

public class GiphyActivity extends Activity {
    
    private Controller controller;
    private GifImageView gifImageView;
    private EditText etQuestion;
    private Button btnAskQuestion;
    private byte[] gifImageInBytes;
    private ProgressBar progressBar;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giphy);

        gifImageView = (GifImageView) findViewById(R.id.gifImageView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        container = (LinearLayout) findViewById(R.id.container);
        container.setVisibility(View.VISIBLE);

        etQuestion = (EditText) findViewById(R.id.etQuestion);
        btnAskQuestion = (Button) findViewById(R.id.btnAskQuestion);

        setButtonListener();

        Intent intent = getIntent();

        controller = (Controller) intent.getSerializableExtra("controller");
    }

    private void setButtonListener() {
        btnAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQuestion.getText().length() <= 0){
                    Toast.makeText(GiphyActivity.this, "You must ask a question!", Toast.LENGTH_SHORT).show();
                }else{
                    new MyTask().execute();
                }
            }
        });
    }

    private class MyTask extends AsyncTask<String, String, byte[]>{

        @Override
        protected void onPreExecute() {
            btnAskQuestion.setEnabled(false);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            container.setAlpha(0.2f);
            gifImageView.stopAnimation();
        }

        private byte[] getByteArrayFromUrl(String url) throws Exception {
            if (url == null) {
                return null;
            }
            return ByteArrayHttpClient.get(url);
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String gifUrl = controller.getGiphy();
            try {
                return getByteArrayFromUrl(gifUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] imageByte) {
            btnAskQuestion.setEnabled(true);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            container.setAlpha(1.0f);
            if (imageByte == null){
                Toast.makeText(GiphyActivity.this, "Oops, your uncle blocked this content, try again!", Toast.LENGTH_LONG);
                gifImageView.startAnimation();
                return;
            }
            gifImageView.setBytes(imageByte);
            gifImageView.startAnimation();
        }
    }
}
