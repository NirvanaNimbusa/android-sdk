package com.paypal.androidsdk;

import android.util.ArrayMap;

import com.braintreepayments.api.exceptions.BraintreeApiErrorResponse;
import com.braintreepayments.api.models.PayPalUAT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

class ValidatePayment {

    private ValidatePayment() {}

    static String createValidationUrl(PayPalUAT payPalUAT, String orderID) {
        return String.format("%s/v2/checkout/orders/%s/validate-payment-method", payPalUAT.getPayPalURL(), orderID);
    }

    static String createValidationPayload(PayPalUAT payPalUAT, String nonce, boolean threeDSecureRequested) {
        String token = payPalUAT.getBearer();
        Map<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", token));

        Map<String, String> tokenParameters = new ArrayMap<>();
        tokenParameters.put("id", nonce);
        tokenParameters.put("type", "NONCE");

        Map<String, Object> paymentSource = new ArrayMap<>();
        paymentSource.put("token", tokenParameters);
        paymentSource.put("contingencies",
                threeDSecureRequested ? Collections.singletonList("3D_SECURE") : Collections.emptyList());

        Map<String, Object> validateParameters = new ArrayMap<>();
        validateParameters.put("payment_source", paymentSource);

        return new JSONObject(validateParameters).toString();
    }

    static String parse3DSContingencyUrl(BraintreeApiErrorResponse errorResponse) {
        String responseBody = errorResponse.getErrorResponse();
        try {
            JSONObject errorJSON = new JSONObject(responseBody);
            JSONArray links = errorJSON.getJSONArray("links");
            for (int i = 0; i < links.length(); i++) {
                JSONObject obj = links.getJSONObject(i);
                String rel = obj.getString("rel");
                if (rel.equalsIgnoreCase("3ds-contingency-resolution")) {
                    String contingencyUrl = obj.getString("href");
                    return contingencyUrl;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
