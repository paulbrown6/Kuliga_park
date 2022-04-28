package com.app.kuliga.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class Weather extends SugarRecord<Weather> {

    @SerializedName("current")
    @Expose
    private Current current;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "current=" + current +
                '}';
    }

    public class Current{

        @SerializedName("temp_c")
        @Expose
        private String temperature;

        @SerializedName("condition")
        @Expose
        private Condition condition;

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        @Override
        public String toString() {
            return "Current{" +
                    "temperature=" + temperature +
                    ", condition=" + condition +
                    '}';
        }
    }

    public class Condition{

        @SerializedName("text")
        @Expose
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Condition{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }
}
