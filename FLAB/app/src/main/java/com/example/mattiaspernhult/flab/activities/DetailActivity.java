package com.example.mattiaspernhult.flab.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mattiaspernhult.flab.controllers.DetailController;
import com.example.mattiaspernhult.flab.fragments.DetailFragment;
import com.example.mattiaspernhult.flab.models.Economi;
import com.example.mattiaspernhult.flab.fragments.NewInputFragment;
import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.fragments.ScanInputFragment;

public class DetailActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private DetailController controller;
    private DetailFragment detailFragment;
    private NewInputFragment inputFragment;
    private ScanInputFragment scanFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initToolbar();
        handleFragment(savedInstanceState);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
    }

    private void handleFragment(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int menu = intent.getIntExtra("menu_choice", -1);
        int tab = intent.getIntExtra("tab_choice", -1);
        FragmentManager fm = getFragmentManager();
        if (menu == 0) {
            if (inputFragment == null) {
                controller = new DetailController(this, tab);
                FragmentTransaction transaction = fm.beginTransaction();
                inputFragment = new NewInputFragment();
                transaction.replace(R.id.container, inputFragment, "INPUT");
                transaction.commit();
                inputFragment.setController(controller);
                inputFragment.setChoice(tab);
                if (savedInstanceState != null) {
                    inputFragment.setValues(savedInstanceState.getString("title"), savedInstanceState.getString("amount"), savedInstanceState.getString("date"));
                }
            }
        } else if (menu == 1) {
            if (detailFragment == null) {
                controller = new DetailController(this, tab);
                FragmentTransaction transaction = fm.beginTransaction();
                detailFragment = new DetailFragment();
                Economi e = (Economi) intent.getSerializableExtra("economi");
                detailFragment.setEconomi(e);
                transaction.replace(R.id.container, detailFragment, "DETAIL");
                transaction.commit();
            }
        } else if (menu == 2) {
            if (scanFragment == null) {
                scanFragment = new ScanInputFragment();
                String content = intent.getStringExtra("scan_content");
                String format = intent.getStringExtra("scan_format");
                controller = new DetailController(this, content, format);
                scanFragment.setController(controller);
                if (savedInstanceState == null) {
                    Economi e = (Economi) intent.getSerializableExtra("economi_scan");
                    if (e == null) {
                        scanFragment.setValues(content, format, null, null, null);
                    } else {
                        scanFragment.setValuesWithCate(content, format, e.getTitle(), String.valueOf(e.getPrice()), e.getCategory());
                    }
                } else {
                    scanFragment.setValues(content, format, savedInstanceState.getString("title_scan"), savedInstanceState.getString("amount_scan"), savedInstanceState.getString("date_scan"));
                }
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.container, scanFragment, "SCAN");
                transaction.commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (inputFragment != null) {
            outState.putString("title", inputFragment.getEtTitle());
            outState.putString("date", inputFragment.getDates());
            outState.putString("amount", inputFragment.getEtAmount());
        }
        if (scanFragment != null) {
            outState.putString("title_scan", scanFragment.getEtTitle());
            outState.putString("date_scan", scanFragment.getDates());
            outState.putString("amount_scan", scanFragment.getEtAmount());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
