package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

public class Amount {

    @SerializedName("currency_code")
    private String mCurrencyCode;

    @SerializedName("value")
    private String mValue;

    public void setCurrencyCode(String currencyCode) {
        this.mCurrencyCode = currencyCode;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

}
