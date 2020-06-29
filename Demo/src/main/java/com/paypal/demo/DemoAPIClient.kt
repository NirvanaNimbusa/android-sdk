package com.paypal.demo

import com.paypal.demo.models.Order
import com.paypal.demo.models.OrderRequest
import com.paypal.demo.models.PayPalUAT
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DemoAPIClient {
    @GET("/id-token")
    fun getPayPalUAT(@Query("countryCode") countryCode: String): Call<PayPalUAT>

    @POST("/order")
    fun fetchOrderID(@Query("countryCode") countryCode: String,
                     @Body orderRequest: OrderRequest?): Call<Order>
}