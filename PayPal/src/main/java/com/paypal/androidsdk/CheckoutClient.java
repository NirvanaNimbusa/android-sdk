package com.paypal.androidsdk;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.BraintreeApiErrorResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCallback;
import com.braintreepayments.api.models.Authorization;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PayPalUAT;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.browserswitch.BrowserSwitchClient;
import com.braintreepayments.browserswitch.BrowserSwitchListener;
import com.braintreepayments.browserswitch.BrowserSwitchResult;

public class CheckoutClient {

    private static final String TAG = "PayPalClient";
    private static final String URL_SCHEME = "com.paypal.demo.braintree";

    private static final int REQUEST_CODE_CARD_CHECKOUT = 1;
    private static final int REQUEST_CODE_PAYPAL_CHECKOUT = 2;

    private PayPalUAT payPalUAT;
    private AuthorizedHttpClient httpClient;

    private BraintreeFragment braintreeFragment;
    private BrowserSwitchClient browserSwitchClient;

    private BrowserSwitchHelper browserSwitchHelper;

    public CheckoutClient(@NonNull String uat, @NonNull FragmentActivity activity) throws InvalidArgumentException {
        payPalUAT = (PayPalUAT) Authorization.fromString(uat);
        braintreeFragment = BraintreeFragment.newInstance(activity, payPalUAT.getBearer());
        httpClient = new AuthorizedHttpClient(payPalUAT.getPayPalURL(), payPalUAT.getBearer());
        browserSwitchClient = BrowserSwitchClient.newInstance(URL_SCHEME);
        browserSwitchHelper = new BrowserSwitchHelper();
    }

    public void payWithCard(
        CardBuilder cardBuilder, final String orderId, final FragmentActivity activity,
        final CheckoutListener listener
    ) {
        Card.tokenize(braintreeFragment, cardBuilder, new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                validatePaymentMethodNonce(paymentMethodNonce, orderId, activity, listener);
            }

            @Override
            public void failure(Exception exception) {
                listener.onCheckoutComplete(exception, null);
            }
        });
    }

    public void onResume(FragmentActivity activity) {
        browserSwitchClient.deliverResult(activity, createBrowserSwitchListener(activity));
    }

    private void validatePaymentMethodNonce(
        PaymentMethodNonce paymentMethodNonce, final String orderId,
        final FragmentActivity activity, final CheckoutListener listener) {
        String path = browserSwitchHelper.buildValidatePaymentUri(orderId, payPalUAT).toString();
        String data = ValidatePayment.createValidationPayload(
                payPalUAT, paymentMethodNonce.getNonce(), true);
        httpClient.post(path, data, new HttpResponseCallback() {
            @Override
            public void success(String responseBody) {
                CheckoutResult result = CardCheckoutResult.newInstance(orderId);
                listener.onCheckoutComplete(null, result);
                Log.d(TAG, String.format("VALIDATION SUCCESS: %S", responseBody));
            }

            @Override
            public void failure(Exception exception) {
                if (exception instanceof BraintreeApiErrorResponse) {
                    BraintreeApiErrorResponse errorResponse = (BraintreeApiErrorResponse) exception;
                    String contingencyUrl = ValidatePayment.parse3DSContingencyUrl(errorResponse);
                    if (contingencyUrl != null) {
                        performCheckoutWithCard3DS(contingencyUrl, activity);
                    } else {
                        listener.onCheckoutComplete(exception, null);
                    }
                } else {
                    listener.onCheckoutComplete(exception, null);
                }
            }
        });
    }

    private void performCheckoutWithCard3DS(String contingencyUrl, FragmentActivity activity) {
        Uri browserSwitchUrl = browserSwitchHelper.buildVerifyThreeDSecureUri(contingencyUrl);
        browserSwitchClient.start(REQUEST_CODE_CARD_CHECKOUT, browserSwitchUrl, activity, createBrowserSwitchListener(activity));
    }

    public void payWithPayPal(String orderId, FragmentActivity activity) {
        Uri browserSwitchUrl = browserSwitchHelper.buildPayPalCheckoutUri(orderId, payPalUAT);
        browserSwitchClient.start(REQUEST_CODE_PAYPAL_CHECKOUT, browserSwitchUrl, activity, createBrowserSwitchListener(activity));
    }

    private static BrowserSwitchListener createBrowserSwitchListener(final FragmentActivity activity) {
        return new BrowserSwitchListener() {
            @Override
            public void onBrowserSwitchResult(int requestCode, BrowserSwitchResult browserSwitchResult, @Nullable Uri uri) {
                if (activity instanceof CheckoutListener) {
                    CheckoutListener listener = ((CheckoutListener) activity);
                    switch (requestCode) {
                        case REQUEST_CODE_CARD_CHECKOUT:
                            CardCheckoutResult cardTokenizationResult = CardCheckoutResult.from(uri);
                            listener.onCheckoutComplete(null, cardTokenizationResult);
                            break;
                        case REQUEST_CODE_PAYPAL_CHECKOUT:
                            if (uri != null) {
                                PayPalCheckoutResult checkoutResult = PayPalCheckoutResult.from(uri);
                                listener.onCheckoutComplete(null, checkoutResult);
                            }
                            break;
                    }
                }
            }
        };
    }
}
