package com.example.mattiaspernhult.flab.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mattiaspernhult.flab.fragments.AddUserFragment;
import com.example.mattiaspernhult.flab.fragments.ListFragment;
import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.controllers.StartController;

public class StartActivity extends ActionBarActivity implements StartController.ChangeActivity {

    private StartController controller;
    private Toolbar toolbar;
    private AddUserFragment addUserFragment;
    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initToolbar();

        addUserFragment = new AddUserFragment();
        listFragment = new ListFragment();

        handleSavedInstance(savedInstanceState);

        controller = new StartController(this, listFragment, addUserFragment);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar);
    }

    private void handleSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("add_mode")) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.container, addUserFragment, "ADD");
                transaction.addToBackStack("ADD");
                addUserFragment.setStrings(savedInstanceState.getString("first"), savedInstanceState.getString("last"));
                transaction.commit();
            } else {
                replaceWithList();
            }
        } else {
            replaceWithList();
        }
    }

    private void replaceWithList() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, listFragment, "LIST");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                getFragmentManager().popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_user) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.container, addUserFragment, "ADD");
            transaction.addToBackStack("ADD");
            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        AddUserFragment a = (AddUserFragment) getFragmentManager().findFragmentByTag("ADD");
        if (a != null && a.isVisible()) {
            outState.putBoolean("add_mode", true);
            outState.putString("first", addUserFragment.getEtFirstText());
            outState.putString("last", addUserFragment.getEtLastText());
        } else {
            outState.putBoolean("add_mode", false);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void change() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void changeFragment() {
        replaceWithList();
    }
}
