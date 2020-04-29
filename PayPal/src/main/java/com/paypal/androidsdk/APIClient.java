package com.paypal.androidsdk;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCallback;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;
import com.paypal.androidsdk.models.CheckoutResult;

public class APIClient {

    private String mUAT;
    private CheckoutCompleteListener mCheckoutCompleteListener;
    private BraintreeFragment mBraintreeFragment;

    public APIClient(AppCompatActivity activity, String uat, CheckoutCompleteListener listener) {
        try {
            String btSandTokenizationKey = "sandbox_tmxhyf7d_dcpspy2brwdjr3qn"; // for development
            mBraintreeFragment = BraintreeFragment.newInstance(activity, btSandTokenizationKey);
            mUAT = uat;
            mCheckoutCompleteListener = listener;
        } catch (InvalidArgumentException e) {
            System.out.println("Failed to initialize API Client.");
        }
    }

    public void checkoutWithCard(final String orderID, final CardBuilder cardBuilder) {

        // Step 1 - tokenize
        CardBuilder testCardBuilder = new CardBuilder()
                .cardholderName("Suzie Smith")
                .cardNumber("4111111111111111")
                .expirationMonth("01")
                .expirationYear("2023")
                .cvv("123")
                .postalCode("12345");

        PaymentMethodNonceCallback tokenizationCallback = new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                Log.d("NONCE:", paymentMethodNonce.getNonce());
            }

            @Override
            public void failure(Exception e) {

            }
        };

        Card.tokenize(mBraintreeFragment, testCardBuilder, tokenizationCallback);


        // Step 2 - call /validate-payment-method

        // Step 3 - check for contingency

        // Step 4 - handle result
        mCheckoutCompleteListener.onCheckoutComplete(new CheckoutResult("order-id", CheckoutResult.CheckoutType.CARD));
    }

}
