package com.paypal.androidsdk;

import androidx.annotation.Nullable;

public interface PayPalCheckoutListener {
    void onResult(@Nullable Exception e, @Nullable PayPalCheckoutResult result);
}
