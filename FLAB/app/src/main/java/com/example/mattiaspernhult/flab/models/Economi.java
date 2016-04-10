package com.example.mattiaspernhult.flab.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-12.
 */
public class Economi implements Serializable {

    private String date;
    private String title;
    private String category;
    private int price;

    public Economi() {

    }

    public Economi(String d, String t, String c, int p) {
        this.date = d;
        this.title = t;
        this.category = c;
        this.price = p;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Economi> getDefaultList() {
        List<Economi> economiList = new ArrayList<>();
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        economiList.add(new Economi("2015-09-12", "Köpte Äpplen", "Mat", 20));
        return economiList;
    }
}
