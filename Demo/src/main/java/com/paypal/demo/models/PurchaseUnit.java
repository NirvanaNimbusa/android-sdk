package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseUnit {

    @SerializedName("amount")
    private Amount mAmount;

    public void setAmount(Amount amount) {
        this.mAmount = amount;
    }
}
