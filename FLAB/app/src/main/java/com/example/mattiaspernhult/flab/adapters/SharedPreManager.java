package com.example.mattiaspernhult.flab.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mattiaspernhult.flab.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-15.
 */
public class SharedPreManager {

    private final static String USERS_LIST = "users";
    private final static String APP = "ECONOMI";
    private final static String USER = "user";

    private static void saveUsers(Context context, List<User> users) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String usersJSON = gson.toJson(users);

        editor.putString(USERS_LIST, usersJSON);
        editor.commit();
    }

    public static void setUser(Context context, String user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, user);
        editor.commit();
        Log.d("debug", "user has been set: " + user);
    }

    public static String getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        if (sharedPreferences.contains(USER)) {
            String userReturn = sharedPreferences.getString(USER, null);
            return userReturn;
        }
        return null;
    }

    public static void addUsers(Context context, User user) {
        List<User> users = getUsers(context);
        if (users == null) {
            users = new ArrayList<User>();
        }
        users.add(user);
        saveUsers(context, users);
    }


    public static List<User> getUsers(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        List<User> users;

        if (sharedPreferences.contains(USERS_LIST)) {
            String usersJSON = sharedPreferences.getString(USERS_LIST, null);
            Gson gson = new Gson();
            User[] userItems = gson.fromJson(usersJSON, User[].class);
            users = Arrays.asList(userItems);
            users = new ArrayList<User>(users);
        } else {
            return null;
        }
        return users;
    }

}
