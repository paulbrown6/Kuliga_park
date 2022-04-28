package com.app.kuliga.data.api.retrofit;

import com.app.kuliga.data.api.entity.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWeather {

    @GET("v1/current.json")
    Call<Weather> getCurrent(@Query("key") String key,
                             @Query("q") String latlon,
                             @Query("aqi") String aqi,
                             @Query("lang") String lang);
}