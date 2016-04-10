package com.example.mattiaspernhult.p2.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mattiaspernhult.p2.R;
import com.example.mattiaspernhult.p2.TextAdapter;
import com.example.mattiaspernhult.p2.constants.Constants;
import com.example.mattiaspernhult.p2.constants.JSONGetter;
import com.example.mattiaspernhult.p2.constants.SharedPref;
import com.example.mattiaspernhult.p2.controllers.Controller;
import com.example.mattiaspernhult.p2.dialogs.NewGroupFragmentDialog;
import com.example.mattiaspernhult.p2.dialogs.NewTextMessageDialog;
import com.example.mattiaspernhult.p2.interfaces.CustomClickListener;
import com.example.mattiaspernhult.p2.interfaces.TextChatListener;
import com.example.mattiaspernhult.p2.models.TextChat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageActivity extends Activity implements TextChatListener, CustomClickListener {

    private ListView listView;
    private Controller controller;
    private String id;
    private ArrayList<TextChat> textChats;
    private TextAdapter textAdapter;

    private static final int PICTURE = 1001;
    private Uri pictureUri;
    private String imagePath;
    private byte[] imageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Log.d("Message", "onCreate");

        listView = (ListView) findViewById(R.id.listViewMessage);

        controller = Constants.controller;

        controller.setTextChatListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (savedInstanceState != null) {
            imagePath = savedInstanceState.getString("image_path");
            ArrayList<TextChat> t = (ArrayList<TextChat>) savedInstanceState.getSerializable("messages");
            if (t != null) {
                textChats = t;
            } else {
                textChats = new ArrayList<>();
            }
        } else {
            textChats = new ArrayList<>();
        }

        textAdapter = new TextAdapter(this, R.layout.text_item, textChats);

        listView.setAdapter(textAdapter);

        Log.d("MainActivity", "id: " + id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (pictureUri != null) {
            outState.putString("image_path", pictureUri.getPath());
        }
        outState.putSerializable("messages", textChats);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_text_message) {
            NewTextMessageDialog dialog = new NewTextMessageDialog();
            dialog.setListener(this);
            dialog.setType(1);
            dialog.show(getFragmentManager(), "NewTextMessageDialog");
        } else if (id == R.id.action_new_image_message) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String filename = "JPEG_" + timestamp + ".jpg";

                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                pictureUri = Uri.fromFile(new File(dir, filename));

                if (pictureUri == null) {
                    Log.d("Message", "pictureUri är null");
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                startActivityForResult(intent, PICTURE);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap getScaled(String pathToPicture, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToPicture, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / width, photoH / height);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPicture, bmOptions);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE && resultCode == Activity.RESULT_OK) {
            if (pictureUri != null) {
                String pathToPicture = pictureUri.getPath();
                Bitmap imageBitmap = getScaled(pathToPicture, 75, 75);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageByteArray = stream.toByteArray();

                NewTextMessageDialog dialog = new NewTextMessageDialog();
                dialog.setListener(this);
                dialog.setType(2);
                dialog.show(getFragmentManager(), "NewTextMessageDialog");
            } else {
                if (imagePath != null) {
                    Bitmap imageBitmap = getScaled(imagePath, 75, 75);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageByteArray = stream.toByteArray();

                    NewTextMessageDialog dialog = new NewTextMessageDialog();
                    dialog.setListener(this);
                    dialog.setType(2);
                    dialog.show(getFragmentManager(), "NewTextMessageDialog");
                }
            }
        }
    }

    @Override
    public void onNewText(TextChat textChat) {
        Log.d("Message", "ÄR INNE I onNewText i MESSAGE ACTIVITY");
        textChats.add(textChat);
        Log.d("Message", textChat.getMessage() + " " + textChat.getGroup() + " " + textChat.getName() + " " + textChat.getImageBuffer());
        textAdapter = new TextAdapter(this, R.layout.text_item, textChats);
        listView.setAdapter(textAdapter);
    }

    @Override
    public void onClick(boolean save, String username) {

    }

    @Override
    public void newGroup(boolean save, String groupname) {
        if (save) {
            new MyTask().execute(Constants.SEND_TEXT, groupname);
        } else {
            new MyTask().execute(Constants.SEND_IMAGE, groupname);
            controller.setImageByteArray(imageByteArray);
        }
    }

    private class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String choice = strings[0];

            switch (choice) {
                case Constants.SEND_TEXT:
                    controller.writeToServer(JSONGetter.sendMessage(id, strings[1]));
                    break;

                case Constants.SEND_IMAGE:
                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    double latitude;
                    double longitude;
                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    } else {
                        longitude = 13.03045189;
                        latitude = 55.60461236;
                    }
                    controller.writeToServer(JSONGetter.sendImageMessage(id, strings[1], longitude, latitude));
                    break;
            }
            return null;
        }
    }
}












