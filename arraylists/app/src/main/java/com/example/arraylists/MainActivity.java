package com.example.arraylists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.arraylists.util.NumberFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "arraylists";
    TextView grandtottv, tvTotalChange, subT, subAmt, subDiff;
    Button btnFX, btnReturn;
    ArrayList<Model> models, subcatmodels;
    ArrayList<Model> archivedmodels = new ArrayList<>();
    ArrayList<Float> difference, subcatdifference;
    ArrayList<String> catArray;
    ArrayList<Float> sum, oldsum;//array holding value by categories
    ArrayList<Float> sumdiff;//array holding value changes by categories
    Float grandsum = 0.0f;//value of total portfolio in base currency
    Float oldGrandsum = 0.0f;
    Float diff = 0.0f;
    Map<String, ArrayList<String >> subcatMap;//array of subcategory titles grouped by category
    Map<String, Map<String, ArrayList<Model>>> catMap;//array of models for each category
    Map<String, ArrayList<Float>> subcatsumMap, oldsubcatsumMap, subcatsumdiffMap;//array of sum value by subcategories
    Map<String, Float> fxmap, archivedfxmap;
    ExpandableListAdapter listAdapter;
    ExpandableListView listView;
    LinearLayout defaultLayout, displayLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grandtottv = findViewById(R.id.grandTotalAmt);
        tvTotalChange = findViewById(R.id.grandTotal_change);
        subT=findViewById(R.id.subtitle_desc);
        subAmt=findViewById(R.id.subgrandTotalAmt);
        subDiff=findViewById(R.id.subgrandTotal_change);
        btnFX = findViewById(R.id.btnFXrefresh);
        btnReturn=findViewById(R.id.btnReturn);
        listView = findViewById(R.id.expandableListView);
        archivedmodels = new ArrayList<>();
        archivedfxmap = new HashMap<>();
        fxmap = new HashMap<>();
        difference = new ArrayList<>();
        subcatdifference = new ArrayList<>();
        defaultLayout=findViewById(R.id.default_layout);
        displayLayout=findViewById(R.id.display_layout);

        displayLayout.setVisibility(View.GONE);
        defaultLayout.setVisibility(View.VISIBLE);
        initialise();

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String category = catArray.get(groupPosition).trim();
                String subcategory = subcatMap.get(category).get(childPosition).trim();
                subT.setText(subcategory);
                Float amount= subcatsumMap.get(category).get(childPosition);
                Float diffamount = subcatsumdiffMap.get(category).get(childPosition);
                subAmt.setText(NumberFormat.floatNo(amount));
                subDiff.setText(NumberFormat.changefloatNo(diffamount));
                if(diffamount>=0){subDiff.setTextColor(Color.BLACK);}else{subDiff.setTextColor(Color.RED);}
                displaySubcat(category,subcategory);

                return false;
            }
        });

        btnFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialise();
                grabLatestFX();
                computeCurrentValues();
                computeDiff();
                displayData();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLayout.setVisibility(View.GONE);
                defaultLayout.setVisibility(View.VISIBLE);
                displayData();
            }
        });
    }

    private void displaySubcat(String category, String subcategory) {
        subcatmodels=new ArrayList<>();
        subcatdifference=new ArrayList<>();
        for (int i = 0; i < models.size(); i++) {
            Model m=models.get(i);
            Model oldm=archivedmodels.get(i);
            String thiscategory=m.getCategory().trim();
            String thissubcategory=m.getSubcategory().trim();
            if(thiscategory.equals(category) && thissubcategory.equals(subcategory)){
                subcatmodels.add(m);
                Float differ=0.0f;
                Float modelAmt=Float.parseFloat(m.getAmount());
                Float archivedmodelAmt=Float.parseFloat(oldm.getAmount());
                differ=modelAmt-archivedmodelAmt;
                subcatdifference.add(differ);
            }
        }

        changeVisibility();
        displayRecycler();
    }

    private void changeVisibility() {
       displayLayout.setVisibility(View.VISIBLE);
       defaultLayout.setVisibility(View.GONE);
    }

    private void displayRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleradapter myadapter = new recycleradapter(this, subcatmodels, subcatdifference);
        recyclerView.setAdapter(myadapter);
    }


    private void displayData() {
        //send info to the display

        listAdapter = new MyExListAdapter(this, catArray, subcatMap, sum, subcatsumMap, sumdiff, subcatsumdiffMap);
        listView.setAdapter(listAdapter);

    }

    private void initialise() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DATA");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot;
                String dataString = ds.child("Current").child("data").getValue(String.class);
                String fxString = ds.child("Current").child("fx").getValue(String.class);
                String olddataString = ds.child("Archived").child("data").getValue(String.class);
                String oldfxString = ds.child("Archived").child("fx").getValue(String.class);

                models=arrayUtil.convertS(dataString);
                fxmap=arrayUtil.convertStringtoMap(fxString);
                computeCurrentValues();

                archivedmodels=arrayUtil.convertS(olddataString);
                archivedfxmap=arrayUtil.convertStringtoMap(oldfxString);
                computeArchivedValues();
                computeDiff();
                displayData();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
        });
    }

    private void computeArchivedValues() {
        oldGrandsum=arrayUtil.sumArray(archivedmodels, archivedfxmap);
        oldsubcatsumMap = arrayUtil.sumDb(archivedmodels, archivedfxmap); //array of converted values
        oldsum = sumbyCat(archivedmodels, archivedfxmap);//array of aggregated value of each category
    }

    private void computeDiff() {
        diff = grandsum-oldGrandsum;
        tvTotalChange.setText(NumberFormat.changefloatNo(diff));
        if(diff>0){tvTotalChange.setTextColor(Color.BLACK);}
        difference=computeArrayDifference();

        sumdiff = new ArrayList<>();
        for (int i = 0; i <catArray.size() ; i++) {
            Float thisdiff = sum.get(i)-oldsum.get(i);
            sumdiff.add(thisdiff);
        }

        subcatsumdiffMap = new HashMap<>();
        ArrayList<Float> subcategoryValues, oldsubcategoryValue, subcategorydiff;
        for (int i = 0; i < catArray.size() ; i++) {
            String category=catArray.get(i);//selected category
            subcategoryValues=subcatsumMap.get(category);
            oldsubcategoryValue=oldsubcatsumMap.get(category);
            subcategorydiff=new ArrayList<>();
            for (int j = 0; j <subcategoryValues.size() ; j++) {
                Float thissubcatSumDiff = subcategoryValues.get(j) - oldsubcategoryValue.get(j);
                subcategorydiff.add(thissubcatSumDiff);
            }subcatsumdiffMap.put(category, subcategorydiff);
        }
    }


    private void computeCurrentValues() {
        grandsum=arrayUtil.sumArray(models, fxmap);
        grandtottv.setText(NumberFormat.floatNo(grandsum));
        catMap = arrayUtil.modelDbMap(models);
        catArray = arrayUtil.produceUniqCat(models); //array list of unique category titles
        subcatMap = arrayUtil.titleDbMap(models); //array of subcategory titles by category
        subcatsumMap = arrayUtil.sumDb(models, fxmap); //array of converted values
        sum = sumbyCat(models, fxmap);//array of aggregated value of each category
        grandsum = arrayUtil.sumArray(models, fxmap);//grand total portfolio value
    }

    private ArrayList<Float> computeArrayDifference() {
        ArrayList<Float> diffarray = new ArrayList<>();
        for (int i = 0; i <models.size() ; i++) {
            Float differ=0.0f;
            Float modelAmt=Float.parseFloat(models.get(i).getAmount());
            Float archivedmodelAmt=Float.parseFloat(archivedmodels.get(i).getAmount());
            differ=modelAmt-archivedmodelAmt;
            diffarray.add(differ);
        }return diffarray;
    }

    private ArrayList<Float> sumbyCat(ArrayList<Model> models, Map<String, Float> fxmapss) {
        ArrayList<Float> sum = new ArrayList<>();
        for (int i = 0; i <catArray.size() ; i++) {
            String catName = catArray.get(i).trim();
            Float v = 0.0f;
            for (int j = 0; j < models.size() ; j++) {
                Model model=models.get(j);
                String currentCat = model.getCategory().trim();
                if(currentCat.equals(catName)){
                    v=v+arrayUtil.convertOneAsset(model,fxmapss);
                }
            }sum.add(v);
        }return sum;
    }

    private void grabLatestFX() {
        final String fx = "https://api.exchangeratesapi.io/";
        //retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(fx)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ExchangeRatesAPI exchangeRatesAPI = retrofit.create(ExchangeRatesAPI.class);
        Call<Fxrates> call = exchangeRatesAPI.getFxrates();
        call.enqueue(new Callback<Fxrates>() {
            @Override
            public void onResponse(Call<Fxrates> call, Response<Fxrates> response) {
                Fxrates fxrates = response.body();
                String x=fxrates.getRates().toString();
                Xrates xrates=fxrates.getRates();
                fxmap=convertStringtoMap(xrates.toString());
                String z=fxrates.getDate().toString();
                z=z.substring(0,10);
            }
            @Override
            public void onFailure(Call<Fxrates> call, Throwable t) {
                        };
    });}

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Map<String, Float> convertStringtoMap(String x) throws NumberFormatException {
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
