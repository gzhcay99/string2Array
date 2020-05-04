package com.example.arraylists;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class arrayUtil {



    public static Float convertOneAsset(Model model, Map<String, Float> fxmap){
        Float amt = Float.parseFloat(model.getAmount());
        String ccy = model.getCurrency().trim();
        Float fx = fxmap.get(ccy);
        Float convertedamt = amt/fx;
            return convertedamt;
        }



    public static ArrayList<Model> convertS(String s){
        String[] y;
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

    public static ArrayList<String> produceUniqCat(ArrayList<Model> models) {

        ArrayList<String> shortList = new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            String x=models.get(i).getCategory().trim();
            if(!shortList.contains(x)){
                shortList.add(x);
            }}
        return shortList;
    }


    public static Float sumArray (ArrayList<Model> inputmodels, Map<String, Float> fxmap){
        Float sum=0.0f;
        for (int i = 0; i <inputmodels.size() ; i++) {
            Model model=inputmodels.get(i);
            sum=sum+convertOneAsset(model, fxmap);
        }return sum;
    }

    public static Map<String, ArrayList<String>> titleDbMap(ArrayList<Model> models){
        Map<String, ArrayList<String>> dbmap = new HashMap<>();
        ArrayList<String> catList = new ArrayList<>();
        catList=produceUniqCat(models);

        for (int i = 0; i < catList.size(); i++) {
            String category = catList.get(i).trim();
            ArrayList<String> subcatlist = new ArrayList<>();
            for (int j = 0; j < models.size(); j++) {
                Model m = models.get(j);
                String thiscategory = m.getCategory().trim();
                if(thiscategory.equals(category)){
                String subcategory = m.getSubcategory().trim();
                    if(!subcatlist.contains(subcategory)){
                    subcatlist.add(subcategory);
                }}
            } dbmap.put(category,subcatlist);
        } return dbmap;
    }

    public static Map<String, ArrayList<Float>> sumDb(ArrayList<Model> models,
            Map<String, Float> fxmap){
        ArrayList<String> catArray;
        catArray = produceUniqCat(models);
        Map<String, ArrayList<String>> dbtitleMap;
        dbtitleMap = titleDbMap(models);
        Map<String, ArrayList<Float>> dbsum = new HashMap<>();
        for (int i = 0; i < catArray.size(); i++) {
            String category = catArray.get(i);
            ArrayList<String> subcatList;
            subcatList = dbtitleMap.get(category);
            ArrayList<Float> sumbysybcat= new ArrayList<>();
            for (int j = 0; j < subcatList.size(); j++) {
                String subcategory = subcatList.get(j);
                Float s = 0.0f;
                for (int k = 0; k < models.size() ; k++) {
                    Model m=models.get(k);
                    String thisCat = m.getCategory().trim();
                    String thissubcat = m.getSubcategory().trim();
                    if(thisCat.equals(category) && thissubcat.equals(subcategory)){
                        s=s+convertOneAsset(m, fxmap);
                    }
            }sumbysybcat.add(s);
            }dbsum.put(category,sumbysybcat);
        }
        return dbsum;
    }

    public static Map<String, Map<String,ArrayList<Model>>> modelDbMap(ArrayList<Model> models){
        ArrayList<String> catArray;
        catArray = produceUniqCat(models);
        Map<String, ArrayList<String>> dbtitleMap = new HashMap<>();
        dbtitleMap = titleDbMap(models);
        Map<String, Map<String, ArrayList<Model>>> tempDbMap = new HashMap<>();
        for (int i = 0; i < catArray.size(); i++) {
            String category = catArray.get(i);
            ArrayList<String> subcatList;
            subcatList = dbtitleMap.get(category);
           Map<String, ArrayList<Model>> modelsubArray = new HashMap<>();
            for (int j = 0; j < subcatList.size(); j++) {
                String subcategory = subcatList.get(j);
                ArrayList<Model> submodelArray = new ArrayList<>();
                for (int k = 0; k < models.size() ; k++) {
                    Model m=models.get(k);
                    String thisCat = m.getCategory().trim();
                    String thissubcat = m.getSubcategory().trim();
                    if(thisCat.equals(category) && thissubcat.equals(subcategory)){
                        submodelArray.add(m);
                    }
                }modelsubArray.put(subcategory, submodelArray);
            }tempDbMap.put(category,modelsubArray);
        }
        return tempDbMap;
    }

    static Map<String, Float> convertStringtoMap(String x) throws NumberFormatException {
        Map<String,Float> fxmaps = new HashMap<>();
        x=x.replace(":","=");
        x=x.replace("{","");
        x=x.replace("}","");
        String[] xnew=x.split(",");
        for (int i = 0; i <xnew.length ; i++) {
            String[] y = xnew[i].split("=");
            Integer integer = y[0].length();
            if(integer>3){y[0]=y[0].substring(integer-3);}
            String key=y[0].trim();
            Float f = 0.0f;
            if (y[1] != null) {
                f = Float.valueOf(y[1].trim());
            }
            fxmaps.put(y[0],f);
        }
        return fxmaps;
    }



}
