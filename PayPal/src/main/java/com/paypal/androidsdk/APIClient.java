package com.paypal.androidsdk;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.CardBuilder;
import com.paypal.androidsdk.interfaces.APIClientCallback;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;

public class APIClient {

    private String mUAT;
    private CheckoutCompleteListener mCheckoutCompleteListener;
    private BraintreeFragment mBraintreeFragment;

    public APIClient(AppCompatActivity activity, String uat) {
        try {
            String btSandTokenizationKey = "sandbox_tmxhyf7d_dcpspy2brwdjr3qn"; // for development
            mBraintreeFragment = BraintreeFragment.newInstance(activity, btSandTokenizationKey);
            mUAT = uat;
        } catch (InvalidArgumentException e) {
            System.out.println("Failed to initialize API Client.");
        }
    }

    public void checkoutWithCard(final String orderID,
                                 final CardBuilder cardBuilder,
                                 final APIClientCallback callback) {

        // Step 1 - tokenize
        CardBuilder newCardBuilder = new CardBuilder()
                .cardholderName("Suzie Smith")
                .cardNumber("4111111111111111")
                .expirationMonth("01")
                .expirationYear("2023")
                .cvv("123")
                .postalCode("12345");

        Card.tokenize(mBraintreeFragment, newCardBuilder);

        // Step 2 - call /validate-payment-method

        // Step 3 - check for contingency

        // Step 4 - handle result
//        callback.onCheckoutComplete(new CheckoutResult("order-id", CheckoutResult.CheckoutType.CARD));
    }
}
