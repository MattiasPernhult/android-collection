package com.example.mattiaspernhult.flab;

import android.content.Context;

import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-20.
 */
public class C {

    public static String getDate(int year, int month, int day) {
        String strMonth;
        String strDay;
        if (month < 10) {
            strMonth = "0" + month;
        } else {
            strMonth = String.valueOf(month);
        }
        if (day < 10) {
            strDay = "0" + day;
        } else {
            strDay = String.valueOf(day);
        }
        return year + "-" + strMonth + "-" + strDay;
    }

    public static List<String> getUserList(Context context) {
        List<User> users = SharedPreManager.getUsers(context);

        if (users == null) {
            users = new ArrayList<User>();
        }

        List<String> usersList = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            usersList.add(user.getFirstName() + " " + user.getLastName());
        }

        return usersList;
    }
}
