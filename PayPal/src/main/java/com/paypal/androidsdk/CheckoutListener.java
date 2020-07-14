package com.paypal.androidsdk;

import androidx.annotation.Nullable;

import com.braintreepayments.api.models.PaymentMethodNonce;

/**
 * OPTION 2: one listener
 */
public interface CheckoutListener {

    void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce);

    void onCheckoutComplete(@Nullable Exception error, @Nullable CheckoutResult result);
}
