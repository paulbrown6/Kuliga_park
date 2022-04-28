package com.app.kuliga.data.api.retrofit;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class App extends Application {

    private final Api api;
    private String serverURL = "https://kuliga-mobile.kuliga-park.ru/";

    private static App instance;

    public App(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(serverURL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public static App getInstance(){
        if(instance == null) instance = new App();
        return instance;
    }

    public String getServerURL() {
        return serverURL;
    }

    public Api getApi() {
        return api;
    }
}
