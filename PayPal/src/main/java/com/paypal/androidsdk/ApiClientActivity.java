package com.paypal.androidsdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;

public class ApiClientActivity extends AppCompatActivity implements BraintreeCancelListener, BraintreeErrorListener, PaymentMethodNonceCreatedListener {

    private final String TAG = "Kanye West";

    static final String EXTRA_NONCE = "com.paypal.androidsdk.ApiClientActivity.EXTRA_NONCE";
    static final String EXTRA_CARD_BUILDER = "com.paypal.androidsdk.ApiClientActivity.EXTRA_CARD_BUILDER";
    static final String EXTRA_UAT_STRING = "com.paypal.androidsdk.ApiClientActivity.EXTRA_UAT_STRING";

    private String mUAT;
    private CardBuilder mCardBuilder;
    private BraintreeFragment mBraintreeFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mUAT = extras.getString(EXTRA_UAT_STRING);
            mCardBuilder = extras.getParcelable(EXTRA_CARD_BUILDER);
        } else {
            // TODO: bail with error?
            finish();
        }

        if (mUAT == null || mCardBuilder == null) {
            // TODO: bail with error?
            finish();
        }

        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, mUAT);
        } catch (InvalidArgumentException e) {
            // TODO: bail with error?
            finish();
        }

        Card.tokenize(mBraintreeFragment, mCardBuilder);
    }

    @Override
    public void onCancel(int requestCode) {
        Log.d(TAG, "onCancel: " + requestCode);

    }

    @Override
    public void onError(Exception error) {
        Log.d(TAG, "onError: " + error.getMessage());
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Log.d(TAG, "onPaymentMethodNonceCreated: " + paymentMethodNonce.getNonce());

        Intent result = new Intent();
        result.putExtra(EXTRA_NONCE, paymentMethodNonce);

        setResult(RESULT_OK, result);
        finish();
    }
}
