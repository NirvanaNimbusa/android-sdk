package com.paypal.demo.models

import com.google.gson.annotations.SerializedName

data class Payee (
    @SerializedName("emailAddress") val emailAddress: String?
)