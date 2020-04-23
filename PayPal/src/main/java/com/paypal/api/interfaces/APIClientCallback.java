package com.paypal.api.interfaces;

import com.paypal.api.models.CheckoutResult;

/**
 * OPTION 1: Callbacks
 */
public interface APIClientCallback {

    void onCheckoutComplete(CheckoutResult result);

    void onCheckoutFailure(Exception exception);

}

