package com.paypal.androidsdk.interfaces;

import androidx.annotation.Nullable;

import com.braintreepayments.api.interfaces.BraintreeListener;
import com.paypal.androidsdk.models.CheckoutResult;

/**
 * OPTION 2: one listener
 */
public interface CheckoutListener {

    void onResult(@Nullable Exception error, @Nullable CheckoutResult result);
}
