package com.paypal.demo;

import com.paypal.demo.models.PayPalUAT;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIClient {

    @GET("/uat")
    Call<PayPalUAT> getPayPalUAT(@Query("countryCode") String countryCode);

}
