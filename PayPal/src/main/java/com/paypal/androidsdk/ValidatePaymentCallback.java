package com.paypal.androidsdk;

/**
 * Interface to handle validate_payment_method callback
 */
public interface ValidatePaymentCallback {

    void on3DSContingency(String url);

    void onValidateSuccess(); //TODO: return custom model for a ValidateResult type, on success

    void onValidateFailure();
}

