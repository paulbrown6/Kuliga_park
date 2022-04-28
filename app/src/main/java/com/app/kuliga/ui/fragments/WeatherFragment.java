package com.app.kuliga.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.api.entity.Weather;
import com.app.kuliga.ui.fragments.viewmodels.WeatherViewModel;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherFragment extends Fragment {

    private WeatherViewModel mViewModel;
    private ImageView image;
    private TextView temperature;
    private TextView condition;
    private TextView date;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        View mView = getView();
        mViewModel.loadWeather();

        image = mView.findViewById(R.id.weather_image);
        temperature = mView.findViewById(R.id.weather_text_temperature);
        condition = mView.findViewById(R.id.weather_text_condition);
        date = mView.findViewById(R.id.weather_text_date);

        mViewModel.getResult().observe(this, new Observer<WeatherViewModel.WeatherResult>() {
            @Override
            public void onChanged(WeatherViewModel.WeatherResult result) {
                if (result == null) {
                    return;
                }
                if (result.getError() != null) {
                    mViewModel.loadWeatherFromDB();
                }
                if (result.getSuccess() != null) {
                    mViewModel.saveWeatherToDB(result.getSuccess());
                    setWeather(result.getSuccess());
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void setWeather(Weather weather){
        if (weather.getCurrent().getTemperature() != null){
            temperature.setText(weather.getCurrent().getTemperature());
        }
        if (weather.getCurrent().getCondition().getText() != null){
            condition.setText(weather.getCurrent().getCondition().getText());
        }
        date.setText(new SimpleDateFormat("dd MMMM, yyyy", myDateFormatSymbols).format(new Date()));
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }

    };

}