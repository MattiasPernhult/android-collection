package com.example.mattiaspernhult.p2.constants;

import android.content.Context;

/**
 * Created by mattiaspernhult on 15-10-08.
 */
public class JSONGetter {

    public static String getGroups() {
        return "{\"type\": \"groups\"}";
    }

    public static String createGroup(String groupName, Context context) {
        return "{\"type\": \"register\", \"group\": \"" + groupName + "\", \"member\": \"" + SharedPref.getUser(context) + "\"}";
    }

    public static String postLocation(String id, double longitude, double latitude) {
        return "{\"type\": \"location\" ,\"id\": \"" + id + "\", \"longitude\": \"" + longitude + "\", \"latitude\": \"" + latitude + "\"}";
    }

    public static String unregister(String id, Context context) {
        return "{\"type\": \"unregister\", \"id\": \"" + id + "\"}";
    }

    public static String sendMessage(String id, String message) {
        return "{\"type\": \"textchat\", \"id\": \"" + id + "\", \"text\": \"" + message + "\"}";
    }

    public static String sendImageMessage(String id, String message, double longitude, double latitude) {
        return "{\"type\": \"imagechat\", \"id\": \"" + id + "\", \"text\": \"" + message + "\", \"longitude\": \"" + longitude + "\", \"latitude\": \"" + latitude + "\"}";
    }
}
