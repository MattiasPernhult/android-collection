package com.example.mattiaspernhult.p2.threads;

import android.util.Log;

import com.example.mattiaspernhult.p2.models.Buffer;

import org.json.JSONObject;

import java.io.DataInputStream;

/**
 * Created by mattiaspernhult on 15-10-08.
 */
public class Listener implements Runnable {

    private boolean run;
    private Buffer buffer;
    private DataInputStream dataInputStream;

    public Listener(Buffer buffer, DataInputStream dataInputStream) {
        this.buffer = buffer;
        this.dataInputStream = dataInputStream;
        this.run = true;
    }

    @Override
    public void run() {
        while (run) {
            try {
                String response = dataInputStream.readUTF();
                JSONObject jsonObject = new JSONObject(response);
                String type = jsonObject.getString("type");
                switch (type) {
                    case "locations":
                    case "location":
                    case "register":
                    case "groups":
                    case "unregister":
                    case "textchat":
                    case "upload":
                    case "imagechat":
                        Log.d("MainActivity", "från servern: " + response);
                        buffer.put(response);
                        break;
                    default:
                        Log.d("MainActivity", "response som inte används: " + response);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.run = false;
    }
}
