package com.paypal.androidsdk;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.braintreepayments.api.models.PayPalUAT;

class UriBuilder {

    private static final String URL_SCHEME = "com.paypal.demo.braintree";

    public Uri buildValidatePaymentUri(@NonNull String orderId, @NonNull PayPalUAT uat) {
        String path = String.format("v2/checkout/orders/%s/validate-payment-method", orderId);
        String baseUrl = uat.getPayPalURL();
        return Uri.parse(baseUrl)
                .buildUpon()
                .appendEncodedPath(path)
                .build();
    }

    public Uri buildVerifyThreeDSecureUri(@NonNull String contingencyUrl) {
        String redirectUri =
                String.format("%s://x-callback-url/paypal-sdk/paypal-checkout", URL_SCHEME);
        return Uri.parse(contingencyUrl)
                .buildUpon()
                .appendQueryParameter("redirect_uri", redirectUri)
                .build();
    }

    public Uri buildPayPalCheckoutUri(@NonNull String orderId, @NonNull PayPalUAT uat) {
        // TODO: implement
        return null;
    }
}
