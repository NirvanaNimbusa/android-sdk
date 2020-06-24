package com.paypal.androidsdk;

import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCallback;
import com.braintreepayments.api.models.Authorization;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PayPalUAT;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;
import com.paypal.androidsdk.interfaces.ValidatePaymentCallback;
import com.paypal.androidsdk.models.CheckoutResult;

//import com.braintreepayments.browserswitch.BrowserSwitchClient;

public class PaymentHandler {

    private PayPalUAT mPayPalUAT;
    private CheckoutCompleteListener mCheckoutCompleteListener;

    private BraintreeFragment mBraintreeFragment;
//    BrowserSwitchClient browserSwitchClient;

    public PaymentHandler(AppCompatActivity activity, String uat, CheckoutCompleteListener listener) throws InvalidArgumentException {
        try {
            mPayPalUAT = (PayPalUAT) Authorization.fromString(uat);
            mBraintreeFragment = BraintreeFragment.newInstance(activity, mPayPalUAT.getBearer());
        } catch (InvalidArgumentException e) {
            // TODO: make sure potential errors here are PP merchant friendly
            System.out.println("Invalid authorization provided: " + e.getMessage());
        }

        mCheckoutCompleteListener = listener;

//        browserSwitchClient = BrowserSwitchClient.newInstance();
    }

    public void checkoutWithCard(
            final String orderID, final CardBuilder cardBuilder, final FragmentActivity activity
    ) {

        // trigger 3ds v1
        CardBuilder testCardBuilder = new CardBuilder()
                .cardholderName("Suzie Smith")
                .cardNumber("4000000000000002")
                .expirationMonth("01")
                .expirationYear("2023")
                .cvv("123");

//        CardBuilder testCardBuilder = new CardBuilder()
//                .cardholderName("Suzie Smith")
//                .cardNumber("4111111111111111")
//                .expirationMonth("01")
//                .expirationYear("2023")
//                .cvv("123");

        // Step 1 - tokenize
        Card.tokenize(mBraintreeFragment, testCardBuilder, new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                Log.d("NONCE:", paymentMethodNonce.getNonce());

                // Step 2 - call /validate-payment-method with nonce and orderID
                ValidatePaymentClient validateClient = new ValidatePaymentClient(mPayPalUAT);
                validateClient.validatePaymentMethod(orderID, paymentMethodNonce.getNonce(), true, new ValidatePaymentCallback() {
                    @Override
                    public void onValidateSuccess() {
                        // if 3DS URL, display it in web view;
                    }

                    @Override
                    public void onValidateFailure() {
                        //
                    }

                    @Override
                    public void on3DSContingency(String contingencyUrl) {
                        String returnUrlScheme = mBraintreeFragment.getReturnUrlScheme();
                        String redirectUri = "http://10.0.2.2:8000";
//                        String redirectUri = String.format("%s://x-callback-url/paypal-sdk/card-contingency", returnUrlScheme);

                        String browserSwitchUrl = Uri.parse(contingencyUrl)
                                .buildUpon()
                                .appendQueryParameter("redirect_uri", redirectUri)
                                .build()
                                .toString();

//                        browserSwitchClient.start(123, Uri.parse(browserSwitchUrl), activity);
                    }
                });

                mCheckoutCompleteListener.onCheckoutComplete(
                        new CheckoutResult("happy-order-id", CheckoutResult.CheckoutType.CARD));
            }

            @Override
            public void failure(Exception e) {

            }
        });

        // Step 3 - check for contingency

        // Step 4 - handle result

    }

}
