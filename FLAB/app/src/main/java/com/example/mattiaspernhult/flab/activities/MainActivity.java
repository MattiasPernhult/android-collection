package com.example.mattiaspernhult.flab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.models.Economi;
import com.example.mattiaspernhult.flab.controllers.MainController;
import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.custom_views.SlidingTabLayout;
import com.example.mattiaspernhult.flab.tabs.Tab2;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends ActionBarActivity implements Tab2.RecyclerClick {

    private Toolbar toolbar;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private FloatingActionMenu fab_menu;
    FloatingActionButton fab_income;
    FloatingActionButton fab_expense;
    FloatingActionButton fab_scan;

    private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = new MainController(this, getSupportFragmentManager());
        Toast.makeText(this, "Welcome, " + SharedPreManager.getUser(this), Toast.LENGTH_LONG).show();
        Toast.makeText(this, mainController.getWelcomeMessage(), Toast.LENGTH_LONG).show();
        initToolbar();
        initViewPager();
        initFab();
        initTabs();
        registerListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                if (mainController.receiptExists(contents)) {
                    Economi e = mainController.getReceipt(contents);
                    startDetailActivityFromScan(2, contents, format, e);
                } else {
                    Economi e = null;
                    startDetailActivityFromScan(2, contents, format, e);
                }
                fab_menu.close(true);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Could not scan, try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainController.updateList();
    }

    private void startDetailActivityFromScan(int menuChoice, String scanContent, String scanFormat, Economi e) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("menu_choice", menuChoice);
        intent.putExtra("scan_content", scanContent);
        intent.putExtra("scan_format", scanFormat);
        intent.putExtra("economi_scan", e);
        startActivity(intent);
    }

    private void startDetailActivity(int menuChoice, int add_choice) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("menu_choice", menuChoice);
        intent.putExtra("tab_choice", add_choice);
        fab_menu.close(true);
        startActivity(intent);
    }

    private void startDetailActivityWithDetail(Economi e, int tab) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("menu_choice", 1);
        intent.putExtra("tab_choice", tab);
        intent.putExtra("economi", e);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_all:
                mainController.setAll();
                break;
            case R.id.action_week:
                mainController.setWeek();
                break;
            case R.id.action_month:
                mainController.setMonth();
                break;
        }

        mainController.updateList();

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mainController.getAdapter());
    }

    private void initFab() {
        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fab_income = (FloatingActionButton) findViewById(R.id.fab_income);
        fab_expense = (FloatingActionButton) findViewById(R.id.fab_expense);
        fab_scan = (FloatingActionButton) findViewById(R.id.fab_scan);
    }

    private void initTabs() {
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
    }

    private void registerListeners() {
        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailActivity(0, 0);
            }
        });

        fab_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailActivity(0, 1);
            }
        });

        fab_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onClick(Economi e, int tab) {
        startDetailActivityWithDetail(e, tab);
    }
}
