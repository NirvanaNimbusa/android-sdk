package com.paypal.demo;

import com.paypal.demo.models.Order;
import com.paypal.demo.models.OrderRequest;
import com.paypal.demo.models.PayPalUAT;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DemoAPIClient {

    @GET("/uat")
    Call<PayPalUAT> getPayPalUAT(@Query("countryCode") String countryCode);

    @POST("/order")
    Call<Order> fetchOrderID(@Query("countryCode") String countryCode,
                             @Body OrderRequest orderRequest);

}
