package com.example.string2array;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    EditText etcat, etsubcat, etdesc, etcoun, etccy, etamt;
    Button btnSave, btnAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gallery);
        etcat = findViewById(R.id.cat);
        etsubcat = findViewById(R.id.subcat);
        etdesc = findViewById(R.id.desc);
        etcoun = findViewById(R.id.coun);
        etccy = findViewById(R.id.ccy);
        etamt = findViewById(R.id.amt);
        btnAdd = findViewById(R.id.btnAdd);
        btnSave = findViewById(R.id.btnSave);

        toast("you clicked something");
        populateLayout();



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg="are you sure you want to save this?";
            }
        });
    }
    private void checkincomg(){
        if(getIntent().hasExtra("category") && getIntent().hasExtra("subcategory") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("country") &&
                getIntent().hasExtra("currency") && getIntent().hasExtra("amount")){
            populateLayout();
        }else {
            String message ="no data attached";
            toast(message);

        };
    }

    private void populateLayout() {
        String category = getIntent().getStringExtra("Category");
        String subcategory = getIntent().getStringExtra("Subcategory");
        String description = getIntent().getStringExtra("Description");
        String country = getIntent().getStringExtra("Country");
        String currency = getIntent().getStringExtra("Currency");
        String amount = getIntent().getStringExtra("Amount");
        String id =  getIntent().getStringExtra("Position");
        String x = "";
        x = NumberFormat.string(amount);
        etamt.setText(x);
        etcat.setText(category);
        etsubcat.setText(subcategory);
        etdesc.setText(description);
        etcoun.setText(country);
        etccy.setText(currency);

    }

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
