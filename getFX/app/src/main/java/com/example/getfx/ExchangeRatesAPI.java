package com.example.getfx;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExchangeRatesAPI {

    @GET("latest?base=CAD")
    Call<Fxrates> getFxrates();
}
