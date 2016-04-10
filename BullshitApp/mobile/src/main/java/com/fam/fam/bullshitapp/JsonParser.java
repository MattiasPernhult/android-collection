package com.fam.fam.bullshitapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class JsonParser {

    public static String parseYesOrNo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String answer = jsonObject.getString("answer");
            return answer;
        } catch (JSONException e) {
            return null;
        }
    }

    public static String parseGiphy(String json) {
        try {
            Random random = new Random();
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray array = jsonRootObject.getJSONArray("data");
            JSONObject jsonObject = array.getJSONObject(random.nextInt(array.length() - 1));
            JSONObject images = jsonObject.getJSONObject("images");
            JSONObject specificImage = images.getJSONObject("original");
            String imageUrl = specificImage.getString("url");
            return imageUrl;
        } catch (JSONException e) {
            return null;
        }
    }

    public static String parseGiphySingle(String json) {
        try {
            Random random = new Random();
            JSONObject jsonRootObject = new JSONObject(json);
            JSONObject data = jsonRootObject.getJSONObject("data");
            JSONObject images = data.getJSONObject("images");
            JSONObject specificImage = images.getJSONObject("original");
            String imageUrl = specificImage.getString("url");
            return imageUrl;
        } catch (JSONException e) {
            return null;
        }
    }
}
