package com.paypal.androidsdk;

import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.internal.BraintreeHttpClient;
import com.braintreepayments.api.models.Authorization;
import com.braintreepayments.api.models.PayPalUAT;


class PayPalValidatePaymentClient {

    private String mUAT;
    private BraintreeHttpClient mHTTPClient;
    private PayPalUAT mPayPalUAT;

    // add logic to hit /validate-payment-method endpoint with NONCE and orderID
    public PayPalValidatePaymentClient(String uat) throws InvalidArgumentException {
        this.mUAT = uat;

        try {
            Authorization auth = new Authorization.fromString(uat);
        } catch (InvalidArgumentException e) {
            throw new InvalidArgumentException("UAT invalid.");
        }
    }

    protected void validatePaymentMethod(String orderID, String nonce, Boolean threeDSecureRequested) {
        BraintreeHttpClient httpClient = new BraintreeHttpClient(mPayPalUAT);
    }


//    fragment.getHttpClient().post(TokenizationClient.versionedPath(
//            TokenizationClient.PAYMENT_METHOD_ENDPOINT + "/" + lookupNonce +
//            "/three_d_secure/authenticate_from_jwt"), body.toString(), new HttpResponseCallback() {
//        @Override
//        public void success(String responseBody) {
//            ThreeDSecureAuthenticationResponse authenticationResponse = ThreeDSecureAuthenticationResponse.fromJson(responseBody);
//
//            // NEXT_MAJOR_VERSION
//            // Remove this line. Pass back lookupCardNonce + error message if there are errors.
//            // Otherwise pass back authenticationResponse.getCardNonce().
//            CardNonce nonce = ThreeDSecureAuthenticationResponse.getNonceWithAuthenticationDetails(responseBody, lookupCardNonce);
//
//            if (authenticationResponse.getErrors() != null) {
//                fragment.sendAnalyticsEvent("three-d-secure.verification-flow.upgrade-payment-method.failure.returned-lookup-nonce");
//                nonce.getThreeDSecureInfo().setErrorMessage(authenticationResponse.getErrors());
//                completeVerificationFlowWithNoncePayload(fragment, nonce);
//            } else {
//                fragment.sendAnalyticsEvent("three-d-secure.verification-flow.upgrade-payment-method.succeeded");
//                completeVerificationFlowWithNoncePayload(fragment, nonce);
//            }
//        }
//
//        @Override
//        public void failure(Exception exception) {
//            fragment.sendAnalyticsEvent("three-d-secure.verification-flow.upgrade-payment-method.errored");
//
//            fragment.postCallback(exception);
//        }
//    });


}
