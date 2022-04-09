package com.example.helloandroid.helper;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Utility {
    public static Request sendRequest(String from, String to, String api_key){
        HttpUrl.Builder queryUrlBuilder = HttpUrl.get("https://api.getgeoapi.com/v2/currency/convert").newBuilder();
        queryUrlBuilder.addQueryParameter("api_key", api_key);
        queryUrlBuilder.addQueryParameter("from",from);
        queryUrlBuilder.addQueryParameter("to",to);
        queryUrlBuilder.addQueryParameter("format","json");

        return new Request.Builder()
                .url(queryUrlBuilder.build())
                .build();
    }
}
