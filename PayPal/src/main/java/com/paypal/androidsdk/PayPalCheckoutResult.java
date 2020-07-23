package com.paypal.androidsdk;

import android.net.Uri;

public class PayPalCheckoutResult implements CheckoutResult {

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

    public String getPayerId() {
        return payerId;
    }

    public String getIntent() {
        return intent;
    }

    public String getOpType() {
        return opType;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getOrderId() {
        // TODO: allow metadata to be forwarded via browser switch request
        return null;
    }
}
