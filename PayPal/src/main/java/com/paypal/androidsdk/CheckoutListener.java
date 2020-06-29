package com.paypal.androidsdk;

import androidx.annotation.Nullable;

/**
 * OPTION 2: one listener
 */
public interface CheckoutListener {

    void onCheckoutComplete(@Nullable Exception error, @Nullable CheckoutResult result);
}
