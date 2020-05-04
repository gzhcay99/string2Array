package com.example.string2array;

import java.util.Date;
import java.util.Map;

public class FxratesMap {
    private Map<String, Float> fxmaps;
    private String base;
    private Date date;

    public Map<String, Float> getFxmaps() {
        return fxmaps;
    }

    public String getBase() {
        return base;
    }

    public Date getDate() {
        return date;
    }
}
