package com.example.arraylists;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.arraylists.util.*;

import java.util.ArrayList;
import java.util.Map;

public class MyExListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> catList;
    private Map<String, ArrayList<String>> subCatmap;
    private ArrayList<Float> catsum;
    private Map<String, ArrayList<Float>> subCatsummap;
    private ArrayList<Float> catsumdiff;
    private Map<String, ArrayList<Float>> subCatsumdiffmap;

    public MyExListAdapter(Context context, ArrayList<String> catList,
                           Map<String, ArrayList<String>> subCatmap,
                           ArrayList<Float> catsum, Map<String,
                            ArrayList<Float>> subCatsummap,
                           ArrayList<Float> catsumdiff,
                           Map<String, ArrayList<Float>> subCatsumdiffmap) {
        this.context = context;
        this.catList = catList;
        this.subCatmap = subCatmap;
        this.catsum = catsum;
        this.subCatsummap = subCatsummap;
        this.catsumdiff = catsumdiff;
        this.subCatsumdiffmap = subCatsumdiffmap;
    }

    public MyExListAdapter() {
    }


    @Override
    public int getGroupCount() {
        return catList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subCatmap.get(catList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return catList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subCatmap.get(catList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String cattitle = (String) getGroup(groupPosition);
        String catsumtitle = NumberFormat.floatNo(catsum.get(groupPosition));
        Float catsumd = catsumdiff.get(groupPosition);
        String diff = NumberFormat.changefloatNo(catsumd);
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_view_layout, null);
        }
        TextView tv = convertView.findViewById(R.id.cat_desc);
        TextView amt = convertView.findViewById(R.id.cat_amount);
        TextView diffamt =convertView.findViewById(R.id.cat_change);
        tv.setText(cattitle);
        amt.setText(catsumtitle);
        diffamt.setText(diff);
        if(catsumd>=0){diffamt.setTextColor(Color.BLUE);}else{diffamt.setTextColor(Color.RED);};
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String subcattitle = (String) getChild(groupPosition, childPosition);
        String catTitle = (String) getGroup(groupPosition);
        ArrayList<Float> temp = subCatsummap.get(catTitle);
        ArrayList<Float> tempdiff = subCatsumdiffmap.get(catTitle);
        String subcatsumtitle = NumberFormat.floatNo(temp.get(childPosition));
        Float tempD=tempdiff.get(childPosition);
        String subcatdiff = NumberFormat.changefloatNo(tempD);
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subcategory_view_layout, null);
        }
        TextView tv = convertView.findViewById(R.id.subcat_desc);
        TextView subcatsum = convertView.findViewById(R.id.subcat_amount);
        TextView subcdiff = convertView.findViewById(R.id.subcat_change);
        tv.setText(subcattitle);
        subcatsum.setText(subcatsumtitle);
        subcdiff.setText(subcatdiff);
        if(tempD>=0){subcdiff.setTextColor(Color.BLUE);}else{subcdiff.setTextColor(Color.RED);}
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
