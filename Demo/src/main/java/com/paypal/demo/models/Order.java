package com.paypal.demo.models;

import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    private String mID;

    @SerializedName("status")
    private String mStatus;

    public String getID() { return mID; }

    public String getStatus() { return mStatus; }

}
