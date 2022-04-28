package com.app.kuliga.data.entity;

import android.util.Log;

import com.app.kuliga.data.api.entity.ResponseEntity;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class Service extends SugarRecord<Service> {

    private static final String TAG = "Service";
    private String serviceId;
    private String name;
    private String categoryName;
    private String price;
    private String timeWork;
    private String tariffTime;

    public String getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeWork() {
        return timeWork;
    }

    public void setTimeWork(String timeWork) {
        this.timeWork = timeWork;
    }

    public String getTariffTime() {
        return tariffTime;
    }

    public void setTariffTime(String tariffTime) {
        this.tariffTime = tariffTime;
    }

    public static List<Service> createListFromXML(ResponseEntity response){
        List<ResponseEntity.Value> values = response.getaPacket().Value;
        if (values != null && values.size() > 1) {
            values = response.getaPacket().Value.get(1).Array.Value;
        } else {
            return new ArrayList<>();
        }
        List<Service> services = new ArrayList<>();
        for (ResponseEntity.Value value: values) {
            String id = null, name = null;
            try {
                id = value.Structure.getValue("id").optional._int + "";
                name = value.Structure.getValue("name").optional.string;
            } catch (Exception e){
                Log.e(TAG, "createFromXML: " + e.getMessage());
            }
            Service service = new Service();
            if (id != null && !id.equals("")) service.setServiceId(id);
            if (name != null && !name.equals("")) service.setCategoryName(name);
            services.add(service);
        }
        return services;
    }

    public void setTariffFromXML(ResponseEntity response){
        List<ResponseEntity.Value> values = response.getaPacket().Value;
        if (values != null && values.size() > 1) {
            try {
                values = response.getaPacket().Value.get(1).Array.Value;
            } catch (Exception e) {
                Log.e(TAG, "setTariffFromXML: " + e.getMessage());
                return;
            }
        } else {
            return;
        }
        String name = null, price = null, timeWork = null, tariffTime = null;
        try {
            name = values.get(0).Structure.getValue("name").optional.string;
            price = values.get(0).Structure.getValue("prices").Array.Value.get(0).
                    Structure.getValue("price").decimal.string;
            timeWork = values.get(0).Structure.getValue("prices").Array.Value.get(0).
                    Structure.getValue("hourname").optional.string;
            tariffTime = values.get(0).Structure.getValue("tariffname").optional.string;
        } catch (Exception e){
            Log.e(TAG, "setTariffFromXML: " + e.getMessage());
        }
        if (name != null && !name.equals("")) this.setName(name);
        if (price != null && !price.equals("")) this.setPrice(price);
        if (timeWork != null && !timeWork.equals("")) this.setTimeWork(timeWork);
        if (tariffTime != null && !tariffTime.equals("")) this.setTariffTime(tariffTime);
    }
}
