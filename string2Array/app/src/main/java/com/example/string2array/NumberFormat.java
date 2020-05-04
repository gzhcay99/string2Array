package com.example.string2array;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.google.firebase.database.collection.LLRBNode;

import java.text.DecimalFormat;

public class NumberFormat {
    public static String string(String s) {
        Float x = Float.parseFloat(s);
        DecimalFormat format = new DecimalFormat("###,###,###; (###,###,###)");
        String y = format.format(x);
        return y;
    }

    public static String floatNo(Float x) {
        DecimalFormat format = new DecimalFormat("###,###,###; (###,###,###)");
        String y = format.format(x);
        return y;
    }

    public static String changefloatNo(Float x) {
        DecimalFormat format = new DecimalFormat("###,###,###; (###,###,###)");
        String y = format.format(x);
        if (x > 0) {y = "+ " + y;}return y;
}}
