package com.example.arraylistmethods;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arraylistmethods.util.*;


public class GalleryActivity extends AppCompatActivity {
    public static final String TAG = "GalleryActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gallery);

        Log.d(TAG, "onCreate:onCreated started");
        EditText etcat, etsubcat, etdesc, etcoun, etccy, etamt;
        etcat = findViewById(R.id.cat);
        etsubcat = findViewById(R.id.subcat);
        etdesc = findViewById(R.id.desc);
        etcoun = findViewById(R.id.coun);
        etccy = findViewById(R.id.ccy);
        etamt = findViewById(R.id.amt);
        checkincomg();
    }
    private void checkincomg(){
        EditText etcat, etsubcat, etdesc, etcoun, etccy, etamt;
        //check incoming includes extra
        Log.d(TAG, "checkincoming: check incoming intent for extras");
        if(getIntent().hasExtra("category") && getIntent().hasExtra("subcategory") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("country") &&
                getIntent().hasExtra("currency") && getIntent().hasExtra("amount")){
            Log.d(TAG, "checkincoming: extras found");
            String category = getIntent().getStringExtra("category");
            String subcategory = getIntent().getStringExtra("subcategory");
            String description = getIntent().getStringExtra("description");
            String country = getIntent().getStringExtra("country");
            String currency = getIntent().getStringExtra("currency");
            String amount = getIntent().getStringExtra("amount");
            //
            etcat = findViewById(R.id.cat);
            etsubcat = findViewById(R.id.subcat);
            etdesc = findViewById(R.id.desc);
            etcoun = findViewById(R.id.coun);
            etccy = findViewById(R.id.ccy);
            etamt = findViewById(R.id.amt);
            String x = "";
            x = NumberFormat.string(amount);
            etamt.setText(x);
            etcat.setText(category);
            etsubcat.setText(subcategory);
            etdesc.setText(description);
            etcoun.setText(country);
            etccy.setText(currency);


        }
    }



}
