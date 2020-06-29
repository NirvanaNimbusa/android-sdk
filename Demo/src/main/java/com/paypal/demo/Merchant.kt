package com.paypal.demo

import com.paypal.demo.models.Order
import com.paypal.demo.models.OrderRequest
import com.paypal.demo.models.PayPalUAT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Merchant {

    private val demoClient=
            RetrofitClient.sharedInstance.create(DemoAPIClient::class.java)

    fun fetchUAT(callback: (error: Throwable?, uat: String?) -> Unit) {

        demoClient.getPayPalUAT("US").enqueue(object: Callback<PayPalUAT> {
            override fun onResponse(call: Call<PayPalUAT>, response: Response<PayPalUAT>) {
                val uatString = response.body()?.uat
                if (uatString == null) {
                    val error = IllegalStateException("UAT Not Found")
                    callback(error, null)
                } else {
                    callback(null, uatString)
                }
            }

            override fun onFailure(call: Call<PayPalUAT>, t: Throwable) {
                callback(t, null)
            }
        })
    }

    fun fetchOrderId(orderRequest: OrderRequest, callback: (error: Throwable?, orderId: String?) -> Unit) {
        demoClient.fetchOrderID("US", orderRequest).enqueue(object: Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                val orderId = response.body()?.id
                if (orderId == null) {
                    val error = IllegalStateException("Order ID Not found")
                    callback(error, null)
                } else {
                    callback(null, orderId)
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                callback(t, null)
            }
        })
    }
}