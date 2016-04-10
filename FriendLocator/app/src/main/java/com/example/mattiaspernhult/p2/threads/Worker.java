package com.example.mattiaspernhult.p2.threads;

import android.util.Log;

import com.example.mattiaspernhult.p2.models.Buffer;
import com.example.mattiaspernhult.p2.controllers.Controller;
import com.example.mattiaspernhult.p2.models.LocationsMember;
import com.example.mattiaspernhult.p2.activities.MainActivity;
import com.example.mattiaspernhult.p2.constants.SharedPref;
import com.example.mattiaspernhult.p2.models.TextChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattiaspernhult on 15-10-08.
 */
public class Worker implements Runnable {

    private boolean run;
    private Buffer buffer;
    private MainActivity mainActivity;
    private Controller controller;

    public Worker(Buffer buffer, MainActivity mainActivity, Controller controller) {
        this.mainActivity = mainActivity;
        this.buffer = buffer;
        this.run = true;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (run) {
            try {
                String response = this.buffer.get();
                JSONObject jsonObject = new JSONObject(response);
                String type = jsonObject.getString("type");
                switch (type) {
                    case "register":
                        this.register(response);
                        break;
                    case "unregister":
                        this.unregister(response);
                        break;
                    case "groups":
                        this.groups(response);
                        break;
                    case "locations":
                        this.locations(response);
                        break;
                    case "location":
                        this.location(response);
                        break;
                    case "textchat":
                        this.textchat(response);
                        break;
                    case "upload":
                        this.upload(response);
                        break;
                    case "imagechat":
                        this.imagechat(response);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void imagechat(String response) {
        Log.d("Message", response);
        Socket socket = null;
        final byte[] downloadImageArray;
        try {

            JSONObject jsonObject = new JSONObject(response);
            String imageId = jsonObject.getString("imageid");
            int port = Integer.valueOf(jsonObject.getString("port"));
            final String name = jsonObject.getString("member");
            final String text = jsonObject.getString("text");
            final String group = jsonObject.getString("group");
            socket = new Socket("195.178.232.7", port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            output.flush();
            output.writeUTF(imageId);
            output.flush();
            downloadImageArray = (byte[]) input.readObject();

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.addTextChat(new TextChat(text, name, group, downloadImageArray));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void unregister(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            controller.removeId(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void upload(String response) {
        // {' “type”:”upload”,'' “imageid”:”IMAGEID”,' '' “port”:”PORT”''}'
        Log.d("Message", response);
        byte[] imageByteArray = controller.getImageByteArray();
        Socket socket = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String imageId = jsonObject.getString("imageid");
            int port = Integer.valueOf(jsonObject.getString("port"));
            socket = new Socket("195.178.232.7", port);


            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            output.writeUTF(imageId);
            output.flush();
            output.writeObject(imageByteArray);
            output.flush();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void textchat(String response) {
        // {"type": "textchat", "id": "Jejdan,Mattias,1446131501092", "text": "ahejbhg"}
        Log.d("MainActivity", "meddelande från servern: " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            final String group = jsonObject.getString("group");
            final String name = jsonObject.getString("member");
            final String text = jsonObject.getString("text");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.addTextChat(new TextChat(text, name, group));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void register(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            controller.addId(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void location(String response) {
    }

    private void locations(String response) {


        try {
            JSONObject jsonObject = new JSONObject(response);
            String group = jsonObject.getString("group");
            JSONArray locations = jsonObject.getJSONArray("location");
            List<LocationsMember> locationsMembers = new ArrayList<>();
            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);
                double realLong = 0;
                double realLat = 0;
                String longitude = location.getString("longitude");
                String latitude = location.getString("latitude");
                if (longitude.equals("NaN")) {
                    realLong = 13.4567;
                } else {
                    realLong = Double.valueOf(longitude);
                }
                if (latitude.equals("NaN")) {
                    realLat = 56.4356;
                } else {
                    realLat = Double.valueOf(latitude);
                }
                locationsMembers.add(new LocationsMember(realLat, realLong, location.getString("member")));
            }
            controller.addLocations(group, locationsMembers);
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.notifyListeners();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void groups(String response) {
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray groups = jsonObject.getJSONArray("groups");
            final List<String> groupsList = new ArrayList<>();
            for (int i = 0; i < groups.length(); i++) {
                JSONObject group = groups.getJSONObject(i);
                groupsList.add(group.getString("group"));
            }

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.setGroups(groupsList);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        this.run = false;
    }
}
