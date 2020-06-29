package com.paypal.demo.models

import com.google.gson.annotations.SerializedName

data class PayPalUAT (
    @SerializedName("id_token") val uat: String? = null
)