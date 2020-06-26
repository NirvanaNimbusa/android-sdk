package com.paypal.demo.models

import com.google.gson.annotations.SerializedName

data class Amount (
    @SerializedName("currency_code") var currencyCode: String?,
    @SerializedName("value") var value: String?
)