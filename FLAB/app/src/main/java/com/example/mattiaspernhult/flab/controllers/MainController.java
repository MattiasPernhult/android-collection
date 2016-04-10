package com.example.mattiaspernhult.flab.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.tabs.Tab1;
import com.example.mattiaspernhult.flab.tabs.Tab2;
import com.example.mattiaspernhult.flab.tabs.Tab3;
import com.example.mattiaspernhult.flab.tabs.Tab4;
import com.example.mattiaspernhult.flab.tabs.Tab5;
import com.example.mattiaspernhult.flab.activities.MainActivity;
import com.example.mattiaspernhult.flab.connections.Dba;
import com.example.mattiaspernhult.flab.connections.RecyclerViewAdapter;
import com.example.mattiaspernhult.flab.models.Economi;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-14.
 */
public class MainController implements Serializable {

    private MainActivity mainActivity;
    private String titles[] = {"INCOMES", "EXPENSES", "EXPENSES CATEGORY", "INCOMES CATEGORY", "SUMMARY"};
    private String[] category = {"Food", "Travel", "Living", "Spare Time", "Others"};
    private String[] categoryIncome = {"Salary", "Others"};
    private final int numberOfTabs = 5;
    private ViewPagerAdapter adapter;
    private RecyclerViewAdapter rAdapterTab1;
    private RecyclerViewAdapter rAdapterTab2;
    private Tab3 gTab3;
    private Tab4 gTab4;
    private Tab5 gTab5;
    private boolean all;
    private boolean week;
    private boolean month;

    public MainController(MainActivity mainActivity, FragmentManager fm) {
        this.mainActivity = mainActivity;
        this.all = true;
        this.week = false;
        this.month = false;
        adapter = new ViewPagerAdapter(fm, this);
    }

    public void setAdapter(RecyclerViewAdapter ra, int tab) {
        if (tab == 1) {
            this.rAdapterTab1 = ra;
        } else if (tab == 2) {
            this.rAdapterTab2 = ra;
        }
    }

    public void setWeek() {
        this.all = false;
        this.month = false;
        this.week = true;
    }

    public void setAll() {
        this.all = true;
        this.month = false;
        this.week = false;
    }

    public void setMonth() {
        this.all = false;
        this.month = true;
        this.week = false;
    }

    public boolean receiptExists(String content) {
        Dba dba = new Dba(mainActivity);
        List<String> contents = dba.getAllReciepts();
        if (contents.size() == 0) {
            return false;
        }
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).equals(content)) {
                return true;
            }
        }
        return false;
    }

    public Economi getReceipt(String content) {
        Dba dba = new Dba(mainActivity);
        Economi economi = dba.getReceipt(content);
        return economi;
    }

    public void updateList() {

        String sinceDate = getDate();

        if (rAdapterTab1 != null) {
            rAdapterTab1.updateList(getIncomes(sinceDate));
        }
        if (rAdapterTab2 != null) {
            rAdapterTab2.updateList(getExpenses(sinceDate));
        }
        if (gTab3 != null) {
            gTab3.updateDiagram(sinceDate);
        }
        if (gTab4 != null) {
            gTab4.updateDiagram(sinceDate);
        }
        if (gTab5 != null) {
            Dba dba = new Dba(mainActivity);
            int totalIncome = dba.getTotalIncome(sinceDate);
            int totalExpense = dba.getTotalExpense(sinceDate);
            int total = totalIncome - totalExpense;
            gTab5.setDataForTextView(String.valueOf(totalIncome), String.valueOf(totalExpense), String.valueOf(total));
        }
    }

    public String getWelcomeMessage() {
        String sinceDate = getDate();
        Dba dba = new Dba(mainActivity);
        int totalIncome = dba.getTotalIncome(sinceDate);
        int totalExpense = dba.getTotalExpense(sinceDate);
        int total = totalIncome - totalExpense;
        if (total > -50 && total < 50) {
            return SharedPreManager.getUser(mainActivity) + ", your balance is okey";
        } else if (total < -10000) {
            return SharedPreManager.getUser(mainActivity) + ", your balance is terrible";
        } else if (total < -1000) {
            return SharedPreManager.getUser(mainActivity) + ", your balance is not good";
        } else if (total > 10000) {
            return SharedPreManager.getUser(mainActivity) + ", your balance is really good";
        } else if (total > 1000) {
            return SharedPreManager.getUser(mainActivity) + ", your balance is good";
        } else {
            return SharedPreManager.getUser(mainActivity) + ", your balance is okey";
        }
    }

    public PieData getDataForCategory(String sinceDate) {
        ArrayList<Entry> amounts = new ArrayList<Entry>();
        ArrayList<String> categories = new ArrayList<String>();

        Dba dba = new Dba(mainActivity);

        if (sinceDate == null) {
            sinceDate = getDate();
        }

        HashMap<String, Integer> hashMap = dba.getAmountForCategory(sinceDate);

        for (int i = 0; i < 5; i++) {
            Integer amount = hashMap.get(category[i]);
            if (amount == null) {
                amount = 0;
            }
            amounts.add(new Entry(amount, i));
            categories.add(category[i]);
        }

        // create pie data set
        PieDataSet dataSet = new PieDataSet(amounts, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(categories, dataSet);

        return data;
    }

    public PieData getDataForCategoryIncome(String sinceDate) {
        ArrayList<Entry> amounts = new ArrayList<Entry>();
        ArrayList<String> categories = new ArrayList<String>();

        Dba dba = new Dba(mainActivity);

        if (sinceDate == null) {
            sinceDate = getDate();
        }

        HashMap<String, Integer> hashMap = dba.getAmountForCategoryIncome(sinceDate);

        Log.d("debug", "storlek: " + hashMap.size());

        for (int i = 0; i < 2; i++) {
            Integer amount = hashMap.get(categoryIncome[i]);
            if (amount == null) {
                amount = 0;
            }
            amounts.add(new Entry(amount, i));
            categories.add(categoryIncome[i]);
        }

        // create pie data set
        PieDataSet dataSet = new PieDataSet(amounts, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);


        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(categories, dataSet);

        return data;
    }

    public String getDate() {
        String sinceDate = "1974-01-01";

        if (!all) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (week) {
                cal.add(Calendar.DAY_OF_MONTH, -7);
                sinceDate = dateFormat.format(cal.getTime());
            } else {
                cal.add(Calendar.MONTH, -1);
                sinceDate = dateFormat.format(cal.getTime());
            }
        }
        return sinceDate;
    }


    public ViewPagerAdapter getAdapter() {
        return this.adapter;
    }

    public List<Economi> getIncomes(String sinceDate) {

        if (sinceDate == null) {
            sinceDate = getDate();
        }

        Dba dba = new Dba(mainActivity);
        return dba.getAllIncomesSince(sinceDate);
    }

    public List<Economi> getExpenses(String sinceDate) {

        if (sinceDate == null) {
            sinceDate = getDate();
        }

        Dba dba = new Dba(mainActivity);
        return dba.getAllExpensesSince(sinceDate);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private MainController mainController;

        private ViewPagerAdapter(FragmentManager fm, MainController controller) {
            super(fm);
            this.mainController = controller;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Tab1 t1 = new Tab1();
                t1.setController(mainController);
                t1.setListener(mainActivity);
                return t1;
            } else if (position == 1) {
                Tab2 t2 = new Tab2();
                t2.setController(mainController);
                t2.setListener(mainActivity);
                return t2;
            } else if (position == 2) {
                gTab3 = new Tab3();
                gTab3.setController(mainController);
                return gTab3;
            } else if (position == 3) {
                gTab4 = new Tab4();
                gTab4.setController(mainController);
                return gTab4;
            } else {
                gTab5 = new Tab5();
                gTab5.setController(mainController);
                return gTab5;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return numberOfTabs;
        }
    }
}
