package com.paypal.androidsdk;

import android.net.Uri;

import com.braintreepayments.api.models.PayPalUAT;

class UriBuilder {

    public Uri buildValidatePaymentUri(String orderId, PayPalUAT uat) {
        String path = String.format("v2/checkout/orders/%s/validate-payment-method", orderId);
        String baseUrl = uat.getPayPalURL();
        return Uri.parse(baseUrl)
                .buildUpon()
                .appendEncodedPath(path)
                .build();
    }

    public Uri buildVerifyThreeDSecureUri(String contingencyUrl) {
        // TODO: implement
        return null;
    }

    public Uri buildPayPalCheckoutUri(String orderId, PayPalUAT uat) {
        // TODO: implement
        return null;
    }
}
