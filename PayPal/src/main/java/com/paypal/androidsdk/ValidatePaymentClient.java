package com.paypal.androidsdk;

import android.util.ArrayMap;
import android.util.Log;

import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.BraintreeHttpClient;
import com.braintreepayments.api.models.PayPalUAT;
import com.paypal.androidsdk.interfaces.ValidatePaymentCallback;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

class ValidatePaymentClient {

    private PayPalHttpClient mHTTPClient;
    private PayPalUAT mPayPalUAT;

    public ValidatePaymentClient(PayPalUAT payPalUAT) {
        this.mPayPalUAT = payPalUAT;
        mHTTPClient = new PayPalHttpClient(mPayPalUAT.getPayPalURL(), payPalUAT.getBearer());
    }

    protected void validatePaymentMethod(String orderID,
                                         String nonce,
                                         Boolean threeDSecureRequested,
                                         ValidatePaymentCallback callback) {
        String token = mPayPalUAT.getBearer();
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

        String path = String.format("%s/v2/checkout/orders/%s/validate-payment-method", mPayPalUAT.getPayPalURL(), orderID);
        String data = new JSONObject(validateParameters).toString();

        // TODO: make actual POST request, see iOS SDK for headers & body format
        mHTTPClient.post(path, data, new HttpResponseCallback() {

            @Override
            public void success(String responseBody) {
                Log.d("SUCCESS", responseBody);
            }

            @Override
            public void failure(Exception exception) {
                Log.d("FAILURE", exception.toString());
            }
        });
    }
}
