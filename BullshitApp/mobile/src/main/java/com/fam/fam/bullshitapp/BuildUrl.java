package com.fam.fam.bullshitapp;

import com.fam.fam.bullshitapp.credentials.ApiKeys;

public class BuildUrl {

    public static String getYesOrNoUrl(){
        return "http://www.yesno.wtf/api";
    }

    public static String getGiphyUrl(String query){
        String url = "http://api.giphy.com/v1/gifs/search?q=" + query + "&api_key=" + ApiKeys.giphyKey + "&limit=100";
        return url;
    }

    public static String getGiphyUrlYoda(String query){
        String url = "http://api.giphy.com/v1/gifs/search?q=" + query + "&api_key=" + ApiKeys.giphyKey + "&limit=20";
        return url;
    }

    public static String getGiphy(){
        return "http://api.giphy.com/v1/gifs/lzLaI8UybeNy0" + "?api_key=" + ApiKeys.giphyKey;
    }

    public static String getIssUrlPosition() {
        return "http://api.open-notify.org/iss-now.json";
    }

    public static String getIssUrlPersons() {
        return "http://api.open-notify.org/astros.json";
    }

    public static String getAdviceUrl() {
        return "http://api.adviceslip.com/advice";
    }

    public static String getYodaUrl(String advice) {
        String validURLString = "https://yoda.p.mashape.com/yoda?sentence=" + advice;
        validURLString = validURLString.replace(' ', '+');
        validURLString = validURLString.replace('"', '\'');
        return validURLString;
    }
}
