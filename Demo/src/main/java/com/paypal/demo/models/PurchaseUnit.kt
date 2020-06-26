package com.paypal.demo.models

import com.google.gson.annotations.SerializedName

data class PurchaseUnit(
    @SerializedName("amount") val amount: Amount?,
    @SerializedName("payee") val payee: Payee?
)