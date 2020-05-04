package com.example.arraylistmethods.util;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExchangeRatesAPI {

    @GET("latest?base=CAD")
    Call<Fxrates> getFxrates();
}
