package com.example.mattiaspernhult.flab.controllers;

import com.example.mattiaspernhult.flab.activities.StartActivity;
import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.fragments.AddUserFragment;
import com.example.mattiaspernhult.flab.fragments.ListFragment;
import com.example.mattiaspernhult.flab.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-15.
 */
public class StartController {

    private StartActivity activity;
    private ChangeActivity listener;
    private ListFragment listFragment;
    private AddUserFragment addUserFragment;

    public StartController(StartActivity startActivity, ListFragment listFragment, AddUserFragment addUserFragment) {
        this.activity = startActivity;
        this.listener = startActivity;
        this.listFragment = listFragment;
        this.addUserFragment = addUserFragment;

        this.listFragment.setController(this);
        this.addUserFragment.setController(this);
    }

    public void setUser(int position) {
        List<User> users = getUsers();
        User user = users.get(position);
        SharedPreManager.setUser(activity, user.getFirstName());
    }

    public List<User> getUsers() {
        return SharedPreManager.getUsers(activity);
    }

    public void changeActivity() {
        listener.change();
    }

    public void addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        SharedPreManager.addUsers(activity, user);
        listener.changeFragment();
    }

    public interface ChangeActivity {
        void change();
        void changeFragment();
    }
}
