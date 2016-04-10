package com.example.mattiaspernhult.flab.connections;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mattiaspernhult.flab.adapters.SharedPreManager;
import com.example.mattiaspernhult.flab.models.Economi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-14.
 */
public class Dba extends SQLiteOpenHelper {

    private Context context;

    public static final String TABLE_INCOME = "incomes";
    public static final String TABLE_EXPENSES = "expenses";
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String DATE = "date";
    public static final String AMOUNT = "amount";
    public static final String USER = "user";

    public static final String DB_NAME = "economi";
    public static final int DB_VERSION = 6;


    public static final String SQL_CREATE_TABLE_SCAN = "CREATE TABLE scan (format text not null, content text not null, user text not null, title text not null, amount integer not null, category text not null);";
    public static final String SQL_CREATE_TABLE_INCOME = "CREATE TABLE " + TABLE_INCOME + " (" + TITLE + " text not null, " + CATEGORY + " text not null, " + DATE + " date not null, " + AMOUNT + " integer not null, " + USER + " text not null);";
    public static final String SQL_CREATE_TABLE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSES + " (" + TITLE + " text not null, " + CATEGORY + " text not null, " + DATE + " date not null, " + AMOUNT + " integer not null, " + USER + " text not null);";


    public Dba(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_EXPENSE);
        db.execSQL(SQL_CREATE_TABLE_INCOME);
        db.execSQL(SQL_CREATE_TABLE_SCAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS scan");
        onCreate(db);
    }

    public void addIncome(Economi economi) {
        SQLiteDatabase db = this.getWritableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "INSERT INTO " + TABLE_INCOME + " VALUES ('" + economi.getTitle() + "', '" + economi.getCategory() + "', '" + economi.getDate() + "', " + economi.getPrice() + ", '" + user + "');";
        db.execSQL(query);
    }

    public void addExpense(Economi economi) {
        SQLiteDatabase db = this.getWritableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "INSERT INTO " + TABLE_EXPENSES + " VALUES ('" + economi.getTitle() + "', '" + economi.getCategory() + "', '" + economi.getDate() + "', " + economi.getPrice() + ", '" + user + "');";
        db.execSQL(query);
    }

    public List<String> getAllReciepts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT content FROM scan WHERE user = '" + user + "';";
        Cursor result = db.rawQuery(query, null);
        List<String> contents = new ArrayList<String>();

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String content = result.getString(result.getColumnIndex("content"));
            contents.add(content);
        }
        return contents;
    }

    public Economi getReceipt(String content) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT title, amount, category FROM scan WHERE user = '" + user + "' AND content = '" + content + "';";
        Cursor result = db.rawQuery(query, null);
        Economi economi = new Economi();
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String title = result.getString(result.getColumnIndex(TITLE));
            String category = result.getString(result.getColumnIndex(CATEGORY));
            int amount = result.getInt(result.getColumnIndex(AMOUNT));
            economi.setTitle(title);
            economi.setCategory(category);
            economi.setPrice(amount);
        }
        return economi;
    }

    public void addReceipts(String content, String format, Economi e) {
        SQLiteDatabase db = this.getWritableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "INSERT INTO scan VALUES ('" + format + "', '" + content + "', '" + user + "', '" + e.getTitle() + "', " + e.getPrice() + ", '" + e.getCategory() + "');";
        db.execSQL(query);
    }

    public HashMap<String, Integer> getAmountForCategory(String sinceDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT category, amount FROM " + TABLE_EXPENSES + " WHERE " + USER + " = '" + user + "' AND date >= '" + sinceDate + "';";
        Cursor result = db.rawQuery(query, null);
        HashMap<String, Integer> hashMap = new HashMap<>();

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String category = result.getString(result.getColumnIndex(CATEGORY));
            Integer amount = result.getInt(result.getColumnIndex(AMOUNT));
            Integer value = hashMap.get(category);
            if (value != null) {
                amount += value;
            }
            hashMap.put(category, amount);
        }
        return hashMap;
    }

    public int getTotalIncome(String sinceDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT amount FROM " + TABLE_INCOME + " WHERE " + USER + " = '" + user + "' AND date >= '" + sinceDate + "';";
        Cursor result = db.rawQuery(query, null);

        int amount = 0;
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            amount += result.getInt(result.getColumnIndex(AMOUNT));
        }
        return amount;
    }

    public int getTotalExpense(String sinceDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT amount FROM " + TABLE_EXPENSES + " WHERE " + USER + " = '" + user + "' AND date >= '" + sinceDate + "';";
        Cursor result = db.rawQuery(query, null);

        int amount = 0;
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            amount += result.getInt(result.getColumnIndex(AMOUNT));
        }
        return amount;
    }

    public HashMap<String, Integer> getAmountForCategoryIncome(String sinceDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT category, amount FROM " + TABLE_INCOME + " WHERE " + USER + " = '" + user + "' AND date >= '" + sinceDate + "';";
        Log.d("debug", query);
        Cursor result = db.rawQuery(query, null);
        HashMap<String, Integer> hashMap = new HashMap<>();
        Integer salary = 0;
        Integer others = 0;

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String category = result.getString(result.getColumnIndex(CATEGORY));
            Integer amount = result.getInt(result.getColumnIndex(AMOUNT));
            Integer value = hashMap.get(category);
            if (value != null) {
                amount += value;
            }
            Log.d("debug", category + " " + amount);
            hashMap.put(category, amount);
        }
        return hashMap;
    }

    public List<Economi> getAllExpensesSince(String sinceDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + USER + " = '" + user + "' AND date >= '" + sinceDate + "';";
        Cursor result = db.rawQuery(query, null);
        List<Economi> es = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String title = result.getString(result.getColumnIndex(TITLE));
            String category = result.getString(result.getColumnIndex(CATEGORY));
            String date = result.getString(result.getColumnIndex(DATE));
            int amount = result.getInt(result.getColumnIndex(AMOUNT));
            Economi e = new Economi(date, title, category, amount);
            es.add(e);
        }
        return es;
    }

    public List<Economi> getAllIncomesSince(String dateSince) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);

        String query = "SELECT * FROM " + TABLE_INCOME + " WHERE " + USER + " = '" + user + "' AND date >= '" + dateSince + "';";
        Cursor result = db.rawQuery(query, null);
        List<Economi> es = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String title = result.getString(result.getColumnIndex(TITLE));
            String category = result.getString(result.getColumnIndex(CATEGORY));
            String date = result.getString(result.getColumnIndex(DATE));
            int amount = result.getInt(result.getColumnIndex(AMOUNT));
            Economi e = new Economi(date, title, category, amount);
            es.add(e);
        }
        return es;
    }

    public List<Economi> getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + USER + " = '" + user + "';";
        Cursor result = db.rawQuery(query, null);
        List<Economi> es = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String title = result.getString(result.getColumnIndex(TITLE));
            String category = result.getString(result.getColumnIndex(CATEGORY));
            String date = result.getString(result.getColumnIndex(DATE));
            int amount = result.getInt(result.getColumnIndex(AMOUNT));
            Economi e = new Economi(date, title, category, amount);
            es.add(e);
        }
        return es;
    }

    public List<Economi> getAllIncomes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = SharedPreManager.getUser(context);
        String query = "SELECT * FROM " + TABLE_INCOME + " WHERE " + USER + " = '" + user + "';";
        Cursor result = db.rawQuery(query, null);
        List<Economi> es = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            String title = result.getString(result.getColumnIndex(TITLE));
            String category = result.getString(result.getColumnIndex(CATEGORY));
            String date = result.getString(result.getColumnIndex(DATE));
            int amount = result.getInt(result.getColumnIndex(AMOUNT));
            Economi e = new Economi(date, title, category, amount);
            es.add(e);
        }
        return es;
    }
}
