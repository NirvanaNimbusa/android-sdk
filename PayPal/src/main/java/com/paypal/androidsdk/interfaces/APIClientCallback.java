package com.paypal.androidsdk.interfaces;

import com.paypal.androidsdk.models.CheckoutResult;

/**
 * OPTION 1: Callbacks
 */
public interface APIClientCallback {

    void onCheckoutComplete(CheckoutResult result);

    void onCheckoutFailure(Exception exception);

}

