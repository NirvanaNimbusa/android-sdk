package com.paypal.androidsdk;

import android.net.Uri;

public class PayPalCheckoutResult {

    private String payerId;
    private String intent;
    private String opType;
    private String token;

    public static PayPalCheckoutResult from(Uri uri) {
        return new PayPalCheckoutResult(uri);
    }

    private PayPalCheckoutResult(Uri uri) {
        payerId = uri.getQueryParameter("PayerID");
        intent = uri.getQueryParameter("intent");
        opType = uri.getQueryParameter("opType");
        token = uri.getQueryParameter("token");
    }
}
