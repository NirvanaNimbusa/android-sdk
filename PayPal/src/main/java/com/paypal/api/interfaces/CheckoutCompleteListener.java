package com.paypal.api.interfaces;

import com.braintreepayments.api.interfaces.BraintreeListener;
import com.paypal.api.models.CheckoutResult;

/**
 * OPTION 2: one listener
 */
public interface CheckoutCompleteListener extends BraintreeListener {

    void onCheckoutComplete(CheckoutResult result);

    void onCheckoutError(Exception exception);

}
