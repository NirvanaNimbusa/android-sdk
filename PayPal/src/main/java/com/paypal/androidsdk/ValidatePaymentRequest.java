package com.paypal.androidsdk;

import com.braintreepayments.api.models.PayPalUAT;
import com.braintreepayments.api.models.PaymentMethodNonce;

class ValidatePaymentRequest {

    ValidatePaymentRequest newInstance(PayPalUAT uat, PaymentMethodNonce paymentMethodNonce, boolean threeDSecureRequested) {
        return new ValidatePaymentRequest(uat, paymentMethodNonce, threeDSecureRequested);
    }

    private ValidatePaymentRequest(PayPalUAT uat, PaymentMethodNonce paymentMethodNonce, boolean threeDSecureRequested) {

    }

    String getHttpRequestData() {
        // TODO: implement
        return null;
    }
}
