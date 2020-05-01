package com.paypal.androidsdk;

import com.braintreepayments.api.internal.BraintreeHttpClient;
import com.braintreepayments.api.models.PayPalUAT;
import com.paypal.androidsdk.interfaces.ValidatePaymentCallback;

class ValidatePaymentClient {

    private BraintreeHttpClient mHTTPClient;
    private PayPalUAT mPayPalUAT;

    public ValidatePaymentClient(PayPalUAT payPalUAT) {
        this.mPayPalUAT = payPalUAT;
        mHTTPClient = new BraintreeHttpClient(mPayPalUAT);
    }

    protected void validatePaymentMethod(String orderID,
                                         String nonce,
                                         Boolean threeDSecureRequested,
                                         ValidatePaymentCallback callback) {
        String url = mPayPalUAT.getPayPalURL() + "/v2/checkout/orders/" + orderID + "/validate-payment-method";

        // TODO: make actual POST request, see iOS SDK for headers & body format

        //

    }

}
