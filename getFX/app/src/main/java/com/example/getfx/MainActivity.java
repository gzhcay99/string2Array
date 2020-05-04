package com.example.getfx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvData;
    Button btnFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = findViewById(R.id.tvData);
        btnFetch = findViewById(R.id.btnFetch);

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
                if(!response.isSuccessful()){
                    tvData.setText("Code: " + response.code());
                    return;
                }

                tvData.setText("");
                Fxrates fxrates = response.body();

                String content ="";
                content += "base: " + fxrates.getBase() + "\n";
                content += "date: " + fxrates.getDate() + "\n";
                content += "CAD: " + fxrates.getRates().getCAD() + "\n";
                content += "HKD: " + fxrates.getRates().getHKD() + "\n";
                content += "GBP: " + fxrates.getRates().getGBP() + "\n";
                content += "CHF: " + fxrates.getRates().getCHF() + "\n";
                content += "MYR: " + fxrates.getRates().getMYR() + "\n";
                content += "CNY: " + fxrates.getRates().getCNY() + "\n";
                content += "USD: " + fxrates.getRates().getUSD() + "\n";
                content += "SGD: " + fxrates.getRates().getSGD() + "\n";
                tvData.append(content);

            }

            @Override
            public void onFailure(Call<Fxrates> call, Throwable t) {
                tvData.setText(t.getMessage());
            }
        });
    }
}
