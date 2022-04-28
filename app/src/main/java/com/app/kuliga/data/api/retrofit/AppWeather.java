package com.app.kuliga.data.api.retrofit;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppWeather extends Application {

    private final ApiWeather api;
    private String serverURL = "http://api.weatherapi.com/";

    private static AppWeather instance;

    public AppWeather(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiWeather.class);
    }

    public static AppWeather getInstance(){
        if(instance == null) instance = new AppWeather();
        return instance;
    }

    public ApiWeather getApi() {
        return api;
    }
}
