package com.app.kuliga.data.entity;

import android.util.Log;

import com.app.kuliga.data.api.entity.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private static final String TAG = "Stock";
    private String id;
    private String base64;
    private String title;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<Stock> createListFromXML(ResponseEntity response){
        List<ResponseEntity.Value> values = response.getaPacket().Value;
        if (values != null && values.size() > 1) {
            try {
                values = response.getaPacket().Value.get(1).Array.Value;
            } catch (Exception e) {
                Log.e(TAG, "setStockFromXML: " + e.getMessage());
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        List<Stock> stocks = new ArrayList<>();
        for (ResponseEntity.Value value: values) {
            String base64 = null, id = null, title = null, url = null;
            try {
                base64 = value.Structure.getValue("image").base64;
                id = value.Structure.getValue("id").optional._int + " ";
                url = value.Structure.getValue("html_link").optional.string;
                title = value.Structure.getValue("title").optional.string;
            } catch (Exception e){
                Log.e(TAG, "setStockFromXML: " + e.getMessage());
            }
            Stock stock = new Stock();
            if (base64 != null && !base64.equals("")) stock.setBase64(base64);
            if (id != null && !id.equals("")) stock.setId(id);
            if (url != null && !url.equals("")) stock.setUrl(url);
            if (title != null && !title.equals("")) stock.setTitle(title);

            stocks.add(stock);
        }
        return stocks;
    }
}
