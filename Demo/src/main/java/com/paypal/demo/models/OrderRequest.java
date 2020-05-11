package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderRequest {

    @SerializedName("intent")
    private String mIntent;

    @SerializedName("purchase_units")
    private ArrayList<PurchaseUnit> mPurchaseUnits;

    public void setIntent(String intent) {
        this.mIntent = intent;
    }

    public void setPurchaesUnits(ArrayList<PurchaseUnit> purchaseUnits) {
        this.mPurchaseUnits = purchaseUnits;
    }
}
