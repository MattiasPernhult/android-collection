package com.fam.fam.bullshitapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Controller implements Serializable {

    private String[] yoda = {"Yoda", "Yoda"};
    private String[] yesOptions = {"yes", "YES"};
    private String[] noOptions = {"no", "NO"};

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public LatLng getISSPosition() {
        String response = HttpManager.getData(BuildUrl.getIssUrlPosition());
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject position = jsonObject.getJSONObject("iss_position");
                LatLng latLng = new LatLng(position.getDouble("latitude"), position.getDouble("longitude"));
                return latLng;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public String getYodaText() {

        String adviceResponse = HttpManager.getData(BuildUrl.getAdviceUrl());

        try {
            JSONObject jsonObject = new JSONObject(adviceResponse);
            JSONObject slip = jsonObject.getJSONObject("slip");
            String advice = slip.getString("advice");
            String yodaResponse = HttpManager.getDataForYoda(BuildUrl.getYodaUrl(advice));
            if (yodaResponse == null) {
                return advice;
            }
            return yodaResponse;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getISSPersons() {
        String response = HttpManager.getData(BuildUrl.getIssUrlPersons());
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                ArrayList<String> persons = new ArrayList<>();
                JSONArray jsonPersons = jsonObject.getJSONArray("people");
                for (int i = 0; i < jsonPersons.length(); i++) {
                    JSONObject person = jsonPersons.getJSONObject(i);
                    if (person.getString("craft").equals("ISS")) {
                        persons.add(person.getString("name"));
                    }
                }
                return persons;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private String getGiphyFromAnswer(String[] options, boolean yoda) throws Exception {
        boolean multi = true;
        Random random = new Random();
        String answer = options[random.nextInt(options.length - 1)];
        String giphyUrl;
        if (yoda) {
            int x = random.nextInt(1000);
            if (x % 10 == 0) {
                giphyUrl = BuildUrl.getGiphy();
                multi = false;
            } else {
                giphyUrl = BuildUrl.getGiphyUrlYoda(answer);
            }
        } else {
            giphyUrl = BuildUrl.getGiphyUrl(answer);
        }
        String giphyResult = HttpManager.getData(giphyUrl);
        if (giphyResult != null) {
            if (multi) {
                return JsonParser.parseGiphy(giphyResult);
            } else {
                return JsonParser.parseGiphySingle(giphyResult);
            }
        }
        return null;
    }

    public String getGiphy() {
        String yesNoResult = HttpManager.getData(BuildUrl.getYesOrNoUrl());
        if (yesNoResult != null) {
            try {
                String answer = JsonParser.parseYesOrNo(yesNoResult);
                String giphyUrl;
                if (answer.equals("yes")) {
                    giphyUrl = getGiphyFromAnswer(yesOptions, false);
                } else {
                    giphyUrl = getGiphyFromAnswer(noOptions, false);
                }
                return giphyUrl;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getYodaGiphy() {
        try {
            String giphyUrl = getGiphyFromAnswer(yoda, true);
            return giphyUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
