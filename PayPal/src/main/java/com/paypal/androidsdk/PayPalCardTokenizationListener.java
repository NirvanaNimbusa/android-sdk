package com.paypal.androidsdk;

import androidx.annotation.Nullable;

public interface PayPalCardTokenizationListener {
    void onResult(@Nullable Exception error, @Nullable PayPalCardTokenizationResult result);
}
