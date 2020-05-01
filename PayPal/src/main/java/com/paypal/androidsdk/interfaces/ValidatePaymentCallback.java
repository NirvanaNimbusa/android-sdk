package com.paypal.androidsdk.interfaces;

/**
 * Interface to handle validate_payment_method callback
 */
public interface ValidatePaymentCallback {

    void onValidateSuccess(); //TODO: return custom model for a ValidateResult type, on success

    void onValidateFailure();

}

