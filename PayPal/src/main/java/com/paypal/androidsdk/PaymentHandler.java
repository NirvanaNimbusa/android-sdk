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

public class PaymentHandler {

    private String mUAT;
    private CheckoutCompleteListener mCheckoutCompleteListener;
    private BraintreeFragment mBraintreeFragment;

    public PaymentHandler(AppCompatActivity activity, String uat, CheckoutCompleteListener listener) {
        try {
            mUAT = uat;
            mBraintreeFragment = BraintreeFragment.newInstance(activity, mUAT);
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


        Card.tokenize(mBraintreeFragment, testCardBuilder, new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                Log.d("NONCE:", paymentMethodNonce.getNonce());
                mCheckoutCompleteListener.onCheckoutComplete(new CheckoutResult("happy-order-id", CheckoutResult.CheckoutType.CARD));
            }

            @Override
            public void failure(Exception e) {

            }
        });

        // Step 2 - call /validate-payment-method

        // Step 3 - check for contingency

        // Step 4 - handle result

    }

}
