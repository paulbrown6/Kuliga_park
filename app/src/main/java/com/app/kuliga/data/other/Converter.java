package com.app.kuliga.data.other;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {
    private static String TAG = "Converter";

    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date date){
        Log.d(TAG, date.toString());
        if (date == null) {
            return "";
        }
        Log.d(TAG, new SimpleDateFormat("yyyyMMdd").format(date));
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static Date stringToDate(String year, String month, String day){
        Log.d(TAG, year + " " + month + " " + day);
        Date date = new Date();
        if (year == null || month == null || day == null) {
            return date;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy.MM.dd");
        try {
            date = format.parse(year + "." + month + "." + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, date.toString());
        return date;
    }
}
