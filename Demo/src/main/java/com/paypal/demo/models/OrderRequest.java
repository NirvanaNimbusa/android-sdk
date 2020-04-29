package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderRequest {

    @SerializedName("intent")
    private String mIntent;

    @SerializedName("purchase_units")
    private ArrayList<PurchaseUnit> mPurchaseUnits;

    @SerializedName("payee")
    private Payee mPayee;

    public void setIntent(String intent) {
        this.mIntent = intent;
    }

    public void setPurchaesUnits(ArrayList<PurchaseUnit> purchaseUnits) {
        this.mPurchaseUnits = purchaseUnits;
    }

    public void setPayee(Payee payee) {
        this.mPayee = payee;
    }

}
