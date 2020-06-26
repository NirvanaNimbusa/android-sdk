package com.paypal.demo.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderRequest(
    @SerializedName("intent") val intent: String?,
    @SerializedName("purchase_units") val purchaseUnit: List<PurchaseUnit>?
)

//class OrderRequest {
//    @SerializedName("intent")
//    var intent: String? = null
//
//    @SerializedName("purchase_units")
//    var purchaseUnits: ArrayList<PurchaseUnit>? = null
//        private set
//
//    fun setPurchaesUnits(purchaseUnits: ArrayList<PurchaseUnit>?) {
//        this.purchaseUnits = purchaseUnits
//    }
//}