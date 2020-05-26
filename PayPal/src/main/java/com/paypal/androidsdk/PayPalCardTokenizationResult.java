package com.paypal.androidsdk;

import android.net.Uri;

public class PayPalCardTokenizationResult {

    public static PayPalCardTokenizationResult from(Uri uri) {
        return new PayPalCardTokenizationResult();
    }
}
