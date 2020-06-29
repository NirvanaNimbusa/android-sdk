package com.paypal.demo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private var instance: Retrofit? = null
        private const val BASE_URL = "https://ppcp-sample-merchant-sand.herokuapp.com"

        val sharedInstance: Retrofit
            get() {
                if (instance == null) {
                    instance = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
                return instance!!
            }
    }
}