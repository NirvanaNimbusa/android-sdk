package com.paypal.androidsdk;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.braintreepayments.api.models.PaymentMethodNonce;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

class ValidatePaymentRequest {

    private String orderId;
    private PaymentMethodNonce paymentMethodNonce;

    private boolean threeDSecureRequested;

    private ValidatePaymentRequest(@NonNull String orderId, @NonNull PaymentMethodNonce paymentMethodNonce, boolean threeDSecureRequested) {
        this.orderId = orderId;
        this.paymentMethodNonce = paymentMethodNonce;
        this.threeDSecureRequested = threeDSecureRequested;
    }

    String getHttpUrl() {
        // TODO: implement
        return null;
    }

    String getHttpBody() {
        Map<String, String> tokenParameters = new ArrayMap<>();
        tokenParameters.put("type", "NONCE");
        tokenParameters.put("id", paymentMethodNonce.getNonce());

        Map<String, Object> paymentSource = new ArrayMap<>();
        paymentSource.put("token", tokenParameters);
        paymentSource.put("contingencies",
                threeDSecureRequested ? Collections.singletonList("3D_SECURE") : Collections.emptyList());

        Map<String, Object> validateParameters = new ArrayMap<>();
        validateParameters.put("payment_source", paymentSource);

        return new JSONObject(validateParameters).toString();
    }

    static class Builder {

        private String orderId;
        private PaymentMethodNonce paymentMethodNonce;
        private boolean threeDSecureRequested;

        Builder orderId(@NonNull String value) {
            orderId = value;
            return this;
        }

        Builder paymentMethodNonce(@NonNull PaymentMethodNonce value) {
            paymentMethodNonce = value;
            return this;
        }

        Builder threeDSecureRequested(boolean value) {
            threeDSecureRequested = value;
            return this;
        }

        ValidatePaymentRequest build() {
            return new ValidatePaymentRequest(orderId, paymentMethodNonce, threeDSecureRequested);
        }
    }
}
