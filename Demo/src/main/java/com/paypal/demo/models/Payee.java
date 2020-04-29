package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

public class Payee {

    @SerializedName("email_address")
    private String mEmailAddress;

    public void setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
    }

}
