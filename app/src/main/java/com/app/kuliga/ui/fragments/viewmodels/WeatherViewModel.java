package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.data.api.entity.Weather;
import com.app.kuliga.data.api.retrofit.ApiCall;

import java.util.List;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<WeatherResult> weatherResult = new MutableLiveData<>();

    public LiveData<WeatherResult> getResult() {
        return weatherResult;
    }

    public void loadWeather(){
        ApiCall.getInstance().getWeather(weatherResult);
    }

    public void loadWeatherFromDB(){
        List<Weather> weather = Weather.listAll(Weather.class);
        if (weather == null || weather.isEmpty()){
            weatherResult.setValue(null);
        } else {
            weatherResult.setValue(new WeatherResult(weather.get(weather.size() - 1)));
        }
    }

    public void saveWeatherToDB(Weather weather){
        deleteWeatherFromDB();
        weather.save();
    }

    public void deleteWeatherFromDB(){
        List<Weather> weather = Weather.listAll(Weather.class);
        if (!(weather == null || weather.isEmpty())) {
            for (Weather weat: weather){
                weat.delete();
            }
        }
    }

    public static class WeatherResult {
        @Nullable
        private Weather success;
        @Nullable
        private String error;

        public WeatherResult(@Nullable String error) {
            this.error = error;
        }

        public WeatherResult(@Nullable Weather success) {
            this.success = success;
        }

        @Nullable
        public Weather getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}