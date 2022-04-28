package com.app.kuliga.data.api.entity;

import com.app.kuliga.data.other.Converter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.List;

@Root(name = "xmlstream", strict = false)
public class ResponseEntity {

    boolean state = false;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Element(name = "APacket", required = false)
    APacket aPacket;

    public APacket getaPacket() {
        return aPacket;
    }

    @Root(name = "ul", strict = false)
    public static class ul {
        @Element(name = "int64_t", required = false)
        public int int64_t;

        @Override
        public String toString() {
            return "ul{" +
                    "int64_t=" + int64_t +
                    '}';
        }
    }

    @Root(name = "Array", strict = false)
    public static class Array {
        @ElementList(name = "Value", required = false, inline = true)
        public List<Value> Value;

        @Override
        public String toString() {
            return "Array{" +
                    "Value=" + Value +
                    '}';
        }
    }

    @Root(name = "Time", strict = false)
    public static class Time {
        @Element(name = "string", required = false)
        public String string;

        @Override
        public String toString() {
            return "Time{" +
                    "string='" + string + '\'' +
                    '}';
        }
    }

    @Root(name = "decimal", strict = false)
    public static class Decimal {
        @Element(name = "string", required = false)
        public String string;

        @Override
        public String toString() {
            return "Decimal{" +
                    "string='" + string + '\'' +
                    '}';
        }
    }

    @Root(name = "Value", strict = false)
    public static class Value {

        @Element(name = "ul", required = false)
        public ul ul;

        @Element(name = "Structure", required = false)
        public Structure Structure;

        @Element(name = "string", required = false)
        public String string;

        @Element(name = "optional", required = false)
        public optional optional;

        @Element(name = "ADate", required = false)
        public ADate ADate;

        @Element(name = "Time", required = false)
        public Time Time;

        @Element(name = "Array", required = false)
        public Array Array;

        @Element(name = "decimal", required = false)
        public Decimal decimal;

        @Element(name = "base64", required = false)
        public String base64;

        @Override
        public String toString() {
            return "Value{" +
                    "ul=" + ul +
                    ", Structure=" + Structure +
                    ", string='" + string + '\'' +
                    ", optional=" + optional +
                    ", ADate=" + ADate +
                    ", base64=" + ADate +
                    '}';
        }
    }

    @Root(name = "optional", strict = false)
    public static class optional {

        @Element(name = "string", required = false)
        public String string;

        @Element(name = "int", required = false)
        public int _int;

        @Element(name = "int64_t", required = false)
        public int int64_t;

        @Override
        public String toString() {
            return "optional{" +
                    "string='" + string + '\'' +
                    ", _int=" + _int +
                    ", int64_t=" + int64_t +
                    '}';
        }
    }

    @Root(name = "Structure", strict = false)
    public static class Structure {

        @ElementList(name = "string", required = false, inline = true)
        public List<String> string;

        @ElementList(name = "Value", required = false, inline = true)
        public List<Value> Value;

        public Value getValue(String name){
            for (int i = 0; i < string.size(); i++) {
                if (string.get(i).equals(name)){
                    return Value.get(i);
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "Structure{" +
                    "string=" + string +
                    ", Value=" + Value +
                    '}';
        }
    }

    @Root(name = "ADate", strict = false)
    public static class ADate {

        @Element(name = "string", required = false)
        public String string;

        public Date getDate(){
            if (string != null)
                return Converter.stringToDate(string.substring(0, 4), string.substring(4, 6), string.substring(6, 8));
            else
                return new Date();
        }

        @Override
        public String toString() {
            return "ADate{" +
                    "string='" + string + '\'' +
                    '}';
        }
    }

    @Root(name = "APacket", strict = false)
    public static class APacket {

        @Element(name = "int", required = false)
        public int _int;

        @ElementList(name = "string", required = false, inline = true)
        public List<String> string;

        @ElementList(name = "Value", required = false, inline = true)
        public List<Value> Value;

        @Override
        public String toString() {
            return "APacket{" +
                    "_int=" + _int +
                    ", string=" + string +
                    ", Value=" + Value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "state=" + state +
                ", aPacket=" + aPacket +
                '}';
    }
}
