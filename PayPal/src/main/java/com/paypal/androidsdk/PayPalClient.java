package com.paypal.androidsdk;

import android.net.Uri;
import android.os.Environment;
import android.provider.Browser;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeApiErrorResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCallback;
import com.braintreepayments.api.models.Authorization;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PayPalUAT;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.browserswitch.BrowserSwitch;
import com.braintreepayments.browserswitch.BrowserSwitchListener;
import com.braintreepayments.browserswitch.BrowserSwitchResult;
import com.braintreepayments.browserswitch.IBrowserSwitchClient;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;
import com.paypal.androidsdk.models.CheckoutResult;

public class PayPalClient {

    private static final String TAG = "PayPalClient";

    private PayPalUAT payPalUAT;
    private BraintreeFragment braintreeFragment;

    private PayPalHttpClient httpClient;
    private IBrowserSwitchClient browserSwitchClient;

    public static PayPalClient newInstance(String uat, FragmentActivity activity) throws InvalidArgumentException {
        return new PayPalClient(uat, activity);
    }

    private PayPalClient(String uat, FragmentActivity activity) throws InvalidArgumentException {
        payPalUAT = (PayPalUAT) Authorization.fromString(uat);
        braintreeFragment = BraintreeFragment.newInstance(activity, payPalUAT.getBearer());
        httpClient = new PayPalHttpClient(payPalUAT.getPayPalURL(), payPalUAT.getBearer());
        browserSwitchClient = BrowserSwitch.newClient();
    }

    public void checkoutWithCard(
        CardBuilder cardBuilder, final String orderId, final FragmentActivity activity,
        final CheckoutCompleteListener listener
    ) {
        Card.tokenize(braintreeFragment, cardBuilder, new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                validatePaymentMethodNonce(paymentMethodNonce, orderId, activity, listener);
            }

            @Override
            public void failure(Exception exception) {
                listener.onCheckoutError(exception);
            }
        });
    }

    private void validatePaymentMethodNonce(
        PaymentMethodNonce paymentMethodNonce, final String orderId,
        final FragmentActivity activity, final CheckoutCompleteListener listener
    ) {
        String path = ValidatePayment.createValidationUrl(payPalUAT, orderId);
        String data = ValidatePayment.createValidationPayload(
                payPalUAT, paymentMethodNonce.getNonce(), true);
        httpClient.post(path, data, new HttpResponseCallback() {
            @Override
            public void success(String responseBody) {
                listener.onCheckoutComplete(
                    new CheckoutResult(orderId, CheckoutResult.CheckoutType.CARD));
                Log.d(TAG, String.format("VALIDATION SUCCESS: %S", responseBody));
            }

            @Override
            public void failure(Exception exception) {
                if (exception instanceof BraintreeApiErrorResponse) {
                    BraintreeApiErrorResponse errorResponse = (BraintreeApiErrorResponse) exception;
                    String contingencyUrl = ValidatePayment.parse3DSContingencyUrl(errorResponse);
                    if (contingencyUrl != null) {
                        listener.onCheckoutValidationRequired();
                        performCheckoutWithCard3DS(contingencyUrl, activity);
                    }
                } else {
                    listener.onCheckoutError(exception);
                }
            }
        });
    }

    private void performCheckoutWithCard3DS(String contingencyUrl, FragmentActivity activity) {
        String returnUrlScheme = braintreeFragment.getReturnUrlScheme();
        String redirectUri =
            String.format("%s://x-callback-url/paypal-sdk/paypal-checkout", returnUrlScheme);

        Uri browserSwitchUrl = Uri.parse(contingencyUrl)
                .buildUpon()
                .appendQueryParameter("redirect_uri", redirectUri)
                .build();
        // NOTE: here, browser switch client assumes activity is also a BrowserSwitchListener
        browserSwitchClient.start(123, browserSwitchUrl, activity, createBrowserSwitchListener(activity));
    }

    public void checkoutWithPayPal(String orderId, FragmentActivity activity) {
        PayPalUAT.Environment environment = payPalUAT.getEnvironment();

        String baseURL = null;
        switch (environment) {
            case PRODUCTION:
                baseURL = "https://www.paypal.com";
                break;
            case SANDBOX:
                baseURL = "https://www.sandbox.paypal.com";
                break;
            case STAGING:
                baseURL = "https://www.msmaster.qa.paypal.com";
                break;
        }

        String returnUrlScheme = braintreeFragment.getReturnUrlScheme();
        String redirectUri =
                String.format("%s://x-callback-url/paypal-sdk/card-contingency", returnUrlScheme);

        Uri browserSwitchUrl = Uri.parse(baseURL)
                .buildUpon()
                .appendPath("checkoutnow")
                .appendQueryParameter("token", orderId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("native_xo", "1")
                .build();

        browserSwitchClient.start(456, browserSwitchUrl, activity, createBrowserSwitchListener(activity));
    }

    public void checkoutWithGooglePay() {

    }

    public void onResume(FragmentActivity activity) {
        browserSwitchClient.deliverResult(activity, createBrowserSwitchListener(activity));
    }

    private static BrowserSwitchListener createBrowserSwitchListener(final FragmentActivity activity) {
        return new BrowserSwitchListener() {
            @Override
            public void onBrowserSwitchResult(int requestCode, BrowserSwitchResult browserSwitchResult, @Nullable Uri uri) {
                if (activity instanceof PayPalCardTokenizationListener) {
                    final PayPalCardTokenizationListener cardTokenizationListener =
                            (PayPalCardTokenizationListener) activity;
                    PayPalCardTokenizationResult cardTokenizationResult = PayPalCardTokenizationResult.from(uri);
                    cardTokenizationListener.onResult(null, cardTokenizationResult);

                } else if (activity instanceof PayPalCheckoutListener) {
                    final PayPalCheckoutListener checkoutListener = (PayPalCheckoutListener) activity;
                    PayPalCheckoutResult checkoutResult = PayPalCheckoutResult.from(uri);
                    checkoutListener.onResult(null, checkoutResult);
                }
            }
        };
    }
}
