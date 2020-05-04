package com.example.string2array;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseDataModel {
    private String date;
    private String data;
    private String fx;

    @Override
    public String toString() {
        return "FirebaseDataModel{" +
                "date='" + date + '\'' +
                ", data='" + data + '\'' +
                ", fx='" + fx + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public FirebaseDataModel() {
    }

    public FirebaseDataModel(String date, String data, String fx) {
        this.date = date;
        this.data = data;
        this.fx = fx;
    }
}
