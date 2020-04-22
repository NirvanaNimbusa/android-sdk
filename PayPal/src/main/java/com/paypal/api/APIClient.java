package com.paypal.api;

import android.util.Log;

import com.braintreepayments.api.models.CardBuilder;

public class APIClient {

    public String mUAT;

    public APIClient(String uat) {
        mUAT = uat;

        CardBuilder cardBuilder = new CardBuilder(); // we can use BT

        Log.d("****Created APIClient.", "");
    }

}
