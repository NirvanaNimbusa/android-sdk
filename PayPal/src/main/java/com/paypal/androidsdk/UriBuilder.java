package com.paypal.androidsdk;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.braintreepayments.api.models.PayPalUAT;

class UriBuilder {

    private static final String URL_SCHEME = "com.paypal.demo.braintree";
    private static final String REDIRECT_URI_THREED_SECURE =
            String.format("%s://x-callback-url/paypal-sdk/card-contingency", URL_SCHEME);
    private static final String REDIRECT_URI_PAYPAL_CHECKOUT =
            String.format("%s://x-callback-url/paypal-sdk/paypal-checkout", URL_SCHEME);

    public Uri buildValidatePaymentUri(@NonNull String orderId, @NonNull PayPalUAT uat) {
        String path = String.format("v2/checkout/orders/%s/validate-payment-method", orderId);
        String baseUrl = uat.getPayPalURL();
        return Uri.parse(baseUrl)
                .buildUpon()
                .appendEncodedPath(path)
                .build();
    }

    public Uri buildVerifyThreeDSecureUri(@NonNull String contingencyUrl) {
        return Uri.parse(contingencyUrl)
                .buildUpon()
                .appendQueryParameter("redirect_uri", REDIRECT_URI_THREED_SECURE)
                .build();
    }

    public Uri buildPayPalCheckoutUri(@NonNull String orderId, @NonNull PayPalUAT uat) {
        PayPalUAT.Environment environment = uat.getEnvironment();

        String baseURL = null;
        switch (environment) {
            case PRODUCTION:
                baseURL = "https://www.paypal.com";
                break;
            case SANDBOX:
                baseURL = "https://www.sandbox.paypal.com";
                break;
            case STAGING:
                baseURL = "https://www.msmaster.qa.paypal.com";
                break;
        }

        return Uri.parse(baseURL)
                .buildUpon()
                .appendPath("checkoutnow")
                .appendQueryParameter("token", orderId)
                .appendQueryParameter("redirect_uri", REDIRECT_URI_PAYPAL_CHECKOUT)
                .appendQueryParameter("native_xo", "1")
                .build();
    }
}
