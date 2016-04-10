package com.example.mattiaspernhult.p2.controllers;

import android.util.Log;

import com.example.mattiaspernhult.p2.interfaces.TextChatListener;
import com.example.mattiaspernhult.p2.models.Buffer;
import com.example.mattiaspernhult.p2.models.LocationsMember;
import com.example.mattiaspernhult.p2.activities.MainActivity;
import com.example.mattiaspernhult.p2.models.TextChat;
import com.example.mattiaspernhult.p2.threads.Listener;
import com.example.mattiaspernhult.p2.threads.Worker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-10-05.
 */
public class Controller implements Serializable {

    private MainActivity mainActivity;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Buffer buffer;
    private HashMap<String, List<LocationsMember>> locations;
    private List<String> ids;

    private Worker worker;
    private Listener listener;

    private UpdateListener updateListener;
    private TextChatListener textChatListener;
    private byte[] imageByteArray;


    public void setListener(UpdateListener listener) {
        this.updateListener = listener;
    }

    public void setTextChatListener(TextChatListener listener) {
        this.textChatListener = listener;
    }

    public void notifyListeners() {
        if (updateListener != null) {
            updateListener.onUpdate();
        }
    }

    public void addTextChat(TextChat textChat) {
        if (textChatListener != null) {
            textChatListener.onNewText(textChat);
        }
    }

    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.buffer = new Buffer();
        this.locations = new HashMap<>();
        this.ids = new ArrayList<>();

        Thread t = new Thread(new InitSocket());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker = new Worker(buffer, mainActivity, this);
        listener = new Listener(buffer, dataInputStream);

        startThreads();
    }

    public void addLocations(String group, List<LocationsMember> members) {
        synchronized (locations) {
            locations.put(group, members);
        }
    }

    public List<LocationsMember> getLocations(String group) {
        synchronized (locations) {
            return locations.get(group);
        }
    }

    public synchronized void writeToServer(String json) {
        try {
            Log.d("MainActivity", "writeToServer: " + json);
            dataOutputStream.writeUTF(json);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startThreads() {
        new Thread(listener).start();
        new Thread(worker).start();
    }

    public void initSocket() {
        try {
            Socket socket = new Socket("195.178.232.7", 7117);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopThreads() {
        worker.stop();
        listener.stop();
    }

    public synchronized void addId(String id) {
        ids.add(id);
    }

    public synchronized List<String> getIds() {
        return ids;
    }

    public synchronized void removeId(String groupId) {
        ids.remove(groupId);
    }

    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

    public byte[] getImageByteArray() {
        return this.imageByteArray;
    }

    private class InitSocket implements Runnable {

        @Override
        public void run() {
            initSocket();
        }
    }

    public interface UpdateListener {
        void onUpdate();
    }

}
