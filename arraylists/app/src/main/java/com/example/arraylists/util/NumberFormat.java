package com.example.arraylists.util;

import java.text.DecimalFormat;

public class NumberFormat {
    public static String string(String s){
        Float x = Float.parseFloat(s);
        DecimalFormat format = new DecimalFormat("###,###,###; (###,###,###)");
        String y = format.format(x);
        return y;
    }

        public static String floatNo(Float x){
            DecimalFormat format = new DecimalFormat("###,###,###; (###,###,###)");
            String y = format.format(x);
            return y;
        }
    public static String changefloatNo(Float x) {
        DecimalFormat format = new DecimalFormat("###,###,###; (###,###,###)");
        String y = format.format(x);
        if (x > 0) {y = "+ " + y;}return y;
    }
}
