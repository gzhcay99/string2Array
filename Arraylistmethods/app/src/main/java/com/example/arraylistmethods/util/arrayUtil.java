package com.example.arraylistmethods.util;



import com.example.arraylistmethods.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class arrayUtil {

    public static ArrayList<String> findUniqueList(ArrayList<String> arrayLong){
        ArrayList<String> arrayShort = new ArrayList<>();

        for (int i = 0; i < arrayLong.size(); i++) {
                    String y = arrayLong.get(i).trim();
                    Boolean count = true;
                    for (int j = 0; j < i; j++) {
                        String z = arrayLong.get(j).trim();
                        if(y.equals(z)){count = false;}
                    }if(count){
                    arrayShort.add(y);}}
        return arrayShort;
    }

    public static String[] getUniCat(ArrayList<Model> models){
        String[] unicatListString = null;
        String[] catListString = null;
        for (int i = 0; i < models.size(); i++) {
           catListString[i] = models.get(i).getCategory();
            } // produce stringList of Category

        ArrayList<String> arrayShort = new ArrayList<>();
        for (int i = 0; i < catListString.length; i++) {
            String y = catListString[i].trim();
            Boolean count = true;
            for (int j = 0; j < i; j++) {
                String z = catListString[j].trim();
                if(y.equals(z)){count = false;}
            }if(count){
                arrayShort.add(y);}} // produce arrayList of unique categories
        for (int i = 0; i <arrayShort.size() ; i++) {
            unicatListString[i]=arrayShort.get(i);
        }
        return unicatListString;
        }


        public static Float convertOneAsset(Model model, Map<String, Float> fxmap){
        Float amt = Float.parseFloat(model.getAmount());
        String ccy = model.getCurrency().trim();
        Float fx = fxmap.get(ccy);
        Float convertedamt = amt/fx;
            return convertedamt;
        }

        public static Map<String, Float> getFXMap (String s){
        Map<String, Float> fxmap = new HashMap<>();
        //convert string to Xrates
            Xrates xrates = new Xrates();

            String[] xx = s.split(",");
            if(xx.length!=12){return null;}//check if s has the right format
            else{
                xrates.setCAD(Float.parseFloat(xx[2]));
                xrates.setCHF(Float.parseFloat(xx[3]));
                xrates.setHKD(Float.parseFloat(xx[4]));
                xrates.setSGD(Float.parseFloat(xx[5]));
                xrates.setUSD(Float.parseFloat(xx[6]));
                xrates.setCNY(Float.parseFloat(xx[7]));
                xrates.setEUR(Float.parseFloat(xx[8]));
                xrates.setGBP(Float.parseFloat(xx[9]));
                xrates.setJPY(Float.parseFloat(xx[10]));
                xrates.setMYR(Float.parseFloat(xx[11]));
        //
            fxmap.put("CAD", xrates.getCAD());
            fxmap.put("CHF", xrates.getCHF());
            fxmap.put("CNY", xrates.getCNY());
            fxmap.put("SGD", xrates.getSGD());
            fxmap.put("USD", xrates.getUSD());
            fxmap.put("GBP", xrates.getGBP());
            fxmap.put("JPY", xrates.getJPY());
            fxmap.put("EUR", xrates.getEUR());
            fxmap.put("MYR", xrates.getMYR());
            fxmap.put("HKD", xrates.getHKD());
            return fxmap;}
        }

        public Xrates getXratesfromString(String s){
        Xrates xrates = new Xrates();

        //check if s has the right format
            String[] xx = s.split(",");
            if(xx.length!=11){return xrates;}
            else{
                xrates.setCAD(Float.parseFloat(xx[2]));
                xrates.setCHF(Float.parseFloat(xx[3]));
                xrates.setHKD(Float.parseFloat(xx[4]));
                xrates.setSGD(Float.parseFloat(xx[5]));
                xrates.setUSD(Float.parseFloat(xx[6]));
                xrates.setCNY(Float.parseFloat(xx[7]));
                xrates.setEUR(Float.parseFloat(xx[8]));
                xrates.setGBP(Float.parseFloat(xx[9]));
                xrates.setJPY(Float.parseFloat(xx[10]));
                xrates.setMYR(Float.parseFloat(xx[11]));
                return xrates;
            }
        }

    public static String converFBtoString(Fxrates fxrates){
        String x=null;
        Xrates xrates=fxrates.getRates();
        x=x+fxrates.getBase();
        x=x+","+fxrates.getDate();
        x=x+","+(xrates.getCAD());
        x=x+","+(xrates.getCHF());
        x=x+","+(xrates.getHKD());
        x=x+","+(xrates.getSGD());
        x=x+","+(xrates.getUSD());
        x=x+","+(xrates.getCNY());
        x=x+","+(xrates.getEUR());
        x=x+","+(xrates.getGBP());
        x=x+","+(xrates.getJPY());
        x=x+","+(xrates.getMYR());

        return x;
    }

    public static ArrayList<Model> convertS(String s){
        String[] y = null;
        ArrayList<Model> arrayList = new ArrayList<Model>();
        Model model = new Model();

        //Category,Subcategory,Description,Country,CCY,Amount,:
        y = s.split(":");

        for (int i = 1; i < y.length; i++) {
            String[] line = y[i].split(",");
            model.setCategory(line[0]);
            model.setSubcategory(line[1]);
            model.setDescription(line[2]);
            model.setCountry(line[3]);
            model.setCurrency(line[4]);
            model.setAmount(line[5]);
            arrayList.add(model);
            model = new Model();
        }return arrayList;
    }

    public static ArrayList<String> catArrays(ArrayList<Model> models){
        ArrayList<String> comArray = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            comArray.add(models.get(i).getCategory());}
        return comArray;}

    public static ArrayList<String> subcatArrays(ArrayList<Model> models){
        ArrayList<String> comArray = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            comArray.add(models.get(i).getSubcategory());}
        return comArray;}

    public static ArrayList<String> ccyArrays(ArrayList<Model> models){
        ArrayList<String> comArray = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            comArray.add(models.get(i).getCurrency());}
        return comArray;}

        public static ArrayList<String> unicat(ArrayList<Model> models){
        //method to produce unique list of categories from an array of Models
        ArrayList<String> shortArray = new ArrayList<>();
        ArrayList<String> longArray = new ArrayList<>();
            for (int i = 0; i <models.size() ; i++) {
                longArray.add(models.get(i).getCategory());
            }
            while(longArray.size()>1){
                String x=longArray.get(0);
                shortArray.add(x);
                longArray.remove(x);
            }
        return shortArray;
    }

    public static ArrayList<Model> catModels (ArrayList<Model> models, ArrayList<String> catArray, int n){
        // method to produce an array of Models within a Category
        ArrayList<Model> subcatArray = new ArrayList<>();
        String category = catArray.get(n).trim();
        for (int i = 0; i < models.size(); i++) {
            String x=models.get(i).getCategory().trim();
            if(category.equals(x)){
                subcatArray.add(models.get(i));
            }
        }
        return subcatArray;
    }

    public static String convertFxratesToString(Fxrates fxrates){
        String string ="";

        return string;
    }
}
