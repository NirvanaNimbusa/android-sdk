package com.paypal.androidsdk.interfaces;

import com.braintreepayments.api.interfaces.BraintreeListener;
import com.paypal.androidsdk.models.CheckoutResult;

/**
 * OPTION 2: one listener
 */
public interface CheckoutCompleteListener {

    void onCheckoutComplete(CheckoutResult result);

    void onCheckoutError(Exception exception);

    void onCheckoutValidationRequired();
}
