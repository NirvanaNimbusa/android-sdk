package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseUnit {

    @SerializedName("amount")
    private Amount mAmount;

    @SerializedName("payee")
    private Payee mPayee;

    public void setAmount(Amount amount) {
        this.mAmount = amount;
    }

    public void setPayee(Payee payee) {
        this.mPayee = payee;
    }
}
