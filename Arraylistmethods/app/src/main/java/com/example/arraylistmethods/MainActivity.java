package com.example.arraylistmethods;

import androidx.appcompat.app.AppCompatActivity;
import com.example.arraylistmethods.util.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_descr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_descr=findViewById(R.id.total_desc);

        String x= getString(R.string.test_data);
        /*
        checkSharedPredDataexist(x); //check if x is already stored in SharedPref
        SharedPreferences spData = getBaseContext().getSharedPreferences("DATA", MODE_PRIVATE);
        String xString = spData.getString("DString", "");
        ArrayList<Model> models = arrayUtil.convertS(xString);
        */
        tv_descr.setText("Hello");


        //grabLatestFX();

    }



    private void checkSharedPredDataexist(String x) {
        SharedPreferences spData = getBaseContext().getSharedPreferences("DATA", MODE_PRIVATE);

        String xString = spData.getString("DString", "");
       
        if(xString==null){
            SharedPreferences.Editor editor = spData.edit();
            editor.putString("DString", x);
            editor.putInt("DVersion", 1);
            editor.apply();
        }            
        }

    private void grabLatestFX() {
        String fx = "https://api.exchangeratesapi.io/";
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
                TextView tvData = findViewById(R.id.total_desc);
                if(!response.isSuccessful()){
                    tvData.setText("Code: " + response.code());
                    return;}

                Fxrates fxrates = response.body();
                String x=arrayUtil.converFBtoString(fxrates);


                SharedPreferences sp = getBaseContext().getSharedPreferences("DATA", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("FXString", x);
                editor.apply(); // save fx rates to a string in sharedpreferences under "FXRATES/FXString
            }

            @Override
            public void onFailure(Call<Fxrates> call, Throwable t) {
                TextView tvData =findViewById(R.id.total_desc);
                tvData.setText(t.getMessage());}
        });
    }

    }
