package com.example.mattiaspernhult.flab.controllers;

import android.content.Context;

import com.example.mattiaspernhult.flab.connections.Dba;
import com.example.mattiaspernhult.flab.models.Economi;

/**
 * Created by mattiaspernhult on 2015-09-14.
 */
public class DetailController {

    private Context context;
    private int choice;
    private String scanContent;
    private String scanFormat;

    public DetailController(Context context, int choice) {
        this.context = context;
        this.choice = choice;
    }

    public DetailController(Context context, String sc, String sf) {
        this.context = context;
        this.scanContent = sc;
        this.scanFormat = sf;
        this.choice = 1;
    }

    public void addReceipt(Economi e) {
        Dba dba = new Dba(context);
        dba.addReceipts(this.scanContent, this.scanFormat, e);
    }

    public void add(Economi e) {
        Dba dba = new Dba(context);
        if (choice == 0) {
            dba.addIncome(e);
        } else if (choice == 1) {
            dba.addExpense(e);
        }
    }
}
