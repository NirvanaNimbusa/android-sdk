package com.paypal.demo.models

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("intent") val intent: String?,
    @SerializedName("purchase_units") val purchaseUnit: List<PurchaseUnit>?
)
