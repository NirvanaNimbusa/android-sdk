package com.paypal.demo.models

import com.google.gson.annotations.SerializedName

data class Order (
    @SerializedName("id") val id: String? = null,
    @SerializedName("status") val status: String? = null
)