package com.example.string2array;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements recycleradapter.OnModelListener {

    EditText etcat, etsubcat, etdesc, etcountry, etccy, etamt;
    TextView tvtotal, tvchange;
    Button btnEDIT, btnDELETE, btnARCHIVE, btnFX, btnCancel, btnSave, btnReset;
    ArrayList<Model> models = new ArrayList<>();
    ArrayList<Model> archivedmodels = new ArrayList<>();
    ArrayList<Float> difference;
    Float grandsum = 0.0f;
    Float oldGrandsum=0.0f;
    Float diff = 0.0f;
    Map<String, ArrayList<String >> subcatMap;
    Map<String, Map<String, ArrayList<Model>>> catMap;
    Map<String, ArrayList<Float>> subcatsumMap;
    Map<String, Float> fxmap, archivedfxmap;
    FirebaseDataModel fbmodel;
    Integer positionID;
    String date="";
    LinearLayout editLayout, displayLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvtotal=findViewById(R.id.grandTotalAmt);
        tvchange=findViewById(R.id.grandTotal_change);
        etcat=findViewById(R.id.cat);
        etsubcat=findViewById(R.id.subcat);
        etdesc=findViewById(R.id.descr);
        etcountry=findViewById(R.id.country);
        etccy=findViewById(R.id.ccy);
        etamt=findViewById(R.id.amt);
        btnEDIT=findViewById(R.id.btn_upload);
        btnDELETE=findViewById(R.id.btnRetrieve);
        btnARCHIVE=findViewById(R.id.btnProcess);
        btnFX=findViewById(R.id.btnFXrefresh);
        btnCancel=findViewById(R.id.btnCancel);
        btnSave=findViewById(R.id.btnSave);
        btnReset=findViewById(R.id.btnReset);
        fbmodel=new FirebaseDataModel();
        models = new ArrayList<>();
        archivedmodels = new ArrayList<>();
        archivedfxmap=new HashMap<>();
        fxmap=new HashMap<>();
        difference=new ArrayList<>();
        editLayout=findViewById(R.id.edit_layout);
        displayLayout=findViewById(R.id.display_layout);

        positionID=0;

        initialise();

        btnEDIT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Model m= new Model();
                        m.setCategory(etcat.getText().toString());
                        m.setSubcategory(etsubcat.getText().toString());
                        m.setDescription(etdesc.getText().toString());
                        m.setCountry(etcountry.getText().toString());
                        m.setCurrency(etccy.getText().toString().trim());
                        m.setAmount(etamt.getText().toString());
                        models.set(positionID, m);
                        clearFields();
                        refresh();
                        changeVisibility();
                    }
                });

        btnDELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model oldm= models.get(positionID);
                models.remove(oldm);
                clearFields();
                refresh();
                changeVisibility();
            }
        });

        btnARCHIVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataString = convertModelstoString(models);
                String fxString = convertFXMAPtoSTring(fxmap);
                if(date.equals("")){date="2020-4-20";};
                saveToFirebase("Archived",
                        dataString,fxString,date);
                saveToFirebase("Current",
                        dataString,fxString,date);
                changeVisibility();
                refresh();
            }
        });

        btnFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabLatestFX();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataString = convertModelstoString(models);
                String fxString = convertFXMAPtoSTring(fxmap);
                if(date.equals("")){date="2020-4-20";};
                saveToFirebase("Current",
                        dataString,fxString,date);
                changeVisibility();
                refresh();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataString = getString(R.string.test_data);
                String fxString = getString(R.string.inifxString);
                saveToFirebase("Archived",
                        dataString,fxString,date);
                saveToFirebase("Current",
                        dataString,fxString,date);
                changeVisibility();
                refresh();
            }
        });
    }
    private void saveToFirebase(String current_or_archived, String datastring, String fxstring, String datestring){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myData = database.getReference("DATA");
        FirebaseDataModel firebaseDataModel = new FirebaseDataModel();
        firebaseDataModel.setData(datastring);
        firebaseDataModel.setDate(datestring);
        firebaseDataModel.setFx(fxstring);
        myData.child(current_or_archived).setValue(firebaseDataModel);
    }

    private void changeVisibility() {
        displayLayout.setVisibility(View.VISIBLE);
        editLayout.setVisibility(View.GONE);
    }

    private void refresh() {
        grandsum=arrayUtil.sumArray(models,fxmap);
        tvtotal.setText(NumberFormat.floatNo(grandsum));
        Float diff=grandsum-oldGrandsum;
        tvchange.setText(NumberFormat.floatNo(diff));
        difference=computeArrayDifference();
        display();
    }

    private void clearFields() {
        etcat.setText("");
        etsubcat.setText("");
        etdesc.setText("");
        etcountry.setText("");
        etccy.setText("");
        etamt.setText("");
    }

    private void display() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleradapter myadapter = new recycleradapter(this, models, difference, this);
        recyclerView.setAdapter(myadapter);
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
               //etcat.setText(dataString);
               //etsubcat.setText(fxString);

               models=arrayUtil.convertS(dataString);
               fxmap=convertStringtoMap(fxString);
               grandsum=arrayUtil.sumArray(models, fxmap);
               tvtotal.setText(NumberFormat.floatNo(grandsum));
               archivedmodels=arrayUtil.convertS(olddataString);
               archivedfxmap=convertStringtoMap(oldfxString);
               oldGrandsum=arrayUtil.sumArray(archivedmodels, archivedfxmap);
               diff = grandsum-oldGrandsum;
               tvchange.setText(NumberFormat.changefloatNo(diff));
               if(diff>0){tvchange.setTextColor(Color.BLACK);}
               difference=computeArrayDifference();;
               display();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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

    private String convertFXMAPtoSTring(Map<String, Float> fxmap) {
        String fxList = getString(R.string.fxsymbols);
        String ss="";
        String[] fxsymbols=fxList.split(",");
        for (int i = 0; i < fxsymbols.length; i++) {
            String key=fxsymbols[i].trim();
            String value=String.valueOf(fxmap.get(key));
            ss=ss+key+":"+value+",";
        }
        return ss;
    }

    private String convertModelstoString(ArrayList<Model> models) {
        String s="";
        s=s+"Category,Subcategory,Description,Country,CCY,Amount:";
        for (int i = 0; i <models.size(); i++) {
            Model model=models.get(i);
            s=s+model.getCategory().trim()+",";
            s=s+model.getSubcategory().trim()+",";
            s=s+model.getDescription().trim()+",";
            s=s+model.getCountry().trim()+",";
            s=s+model.getCurrency().trim()+",";
            s=s+model.getAmount().trim()+":";
        }
        return s;
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

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onModelClick(int position) {
        positionID = position;
        Model model=models.get(position);
        etcat.setText(model.getCategory().trim());
        etsubcat.setText(model.getSubcategory().trim());
        etdesc.setText(model.getDescription().trim());
        etcountry.setText(model.getCountry().trim());
        etccy.setText(model.getCurrency().trim());
        String amt = NumberFormat.string(model.getAmount());
        etamt.setText(amt);
        displayLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);

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

                if(!response.isSuccessful()){
                    return;}
                Fxrates fxrates = response.body();
                Xrates xrates=fxrates.getRates();
                fxmap=convertStringtoMap(xrates.toString());
                String z=fxrates.getDate().toString();
                date=z.substring(0,10);
                etsubcat.setText(xrates.toString());
                refresh();
            }
            @Override
            public void onFailure(Call<Fxrates> call, Throwable t) {

            };
        });}

}
