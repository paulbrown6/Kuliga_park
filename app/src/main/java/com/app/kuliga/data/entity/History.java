package com.app.kuliga.data.entity;

import android.util.Log;

import com.app.kuliga.data.other.Converter;
import com.app.kuliga.data.api.entity.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History {

    private static final String TAG = "History";
    private String amount;
    private String comment;
    private String time;
    private String date;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        if (time != null) {
            return time.substring(0, 2) + ":" + time.substring(2,4);
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        if (date != null)
            return Converter.stringToDate(date.substring(0, 4), date.substring(4, 6), date.substring(6, 8));
        else
            return new Date();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static List<History> createListFromXML(ResponseEntity response){
        List<ResponseEntity.Value> values = response.getaPacket().Value;
        if (values != null && values.size() > 1) {
            try {
                values = response.getaPacket().Value.get(1).Array.Value;
            } catch (Exception e) {
                Log.e(TAG, "setHistoryFromXML: " + e.getMessage());
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        List<History> histories = new ArrayList<>();
        for (ResponseEntity.Value value: values) {
            String amount = null, comment = null, currency = null, date = null, time = null;
            try {
                amount = value.Structure.getValue("amount").decimal.string.split("\\.")[0];
                comment = value.Structure.getValue("comment").string;
                currency = value.Structure.getValue("currency").string;
                date = value.Structure.getValue("date").Time.string.split("T")[0];
                time = value.Structure.getValue("date").Time.string.split("T")[1];
            } catch (Exception e){
                Log.e(TAG, "setHistoryFromXML: " + e.getMessage());
            }
            History history = new History();
            if (amount != null && !amount.equals("")) {
                history.setAmount(amount);
                if (currency != null && !currency.equals("")) history.setAmount(amount + " " + currency);
            }
            if (comment != null && !comment.equals("")) history.setComment(comment);
            if (date != null && !date.equals("")) history.setDate(date);
            if (time != null && !time.equals("")) history.setTime(time);

            histories.add(history);
        }
        return histories;
    }
}
