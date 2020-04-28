package com.paypal.androidsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.util.UUID;

import static com.paypal.androidsdk.ApiClientActivity.EXTRA_CARD_BUILDER;
import static com.paypal.androidsdk.ApiClientActivity.EXTRA_NONCE;
import static com.paypal.androidsdk.ApiClientActivity.EXTRA_UAT_STRING;

public class APIClientFragment extends Fragment {

    private final String TAG = "Kanye West";

    private static final String TAG_PREFIX = "APIClientFragment";

    private String mUat;
    private Context mContext;

    public APIClientFragment() {}

    public static APIClientFragment newInstance(AppCompatActivity activity, String uat) throws InvalidArgumentException {
        if (activity == null) {
            throw new InvalidArgumentException("Activity can not be null.");
        }
        return newInstance(activity, activity.getSupportFragmentManager(), uat);
    }

    public static APIClientFragment newInstance(FragmentActivity activity, String uat) throws InvalidArgumentException {
        if (activity == null) {
            throw new InvalidArgumentException("Activity can not be null.");
        }
        return newInstance(activity, activity.getSupportFragmentManager(), uat);
    }

    public static APIClientFragment newInstance(Fragment fragment, String uat) throws InvalidArgumentException {
        if (fragment == null) {
            throw new InvalidArgumentException("Fragment can not be null.");
        }
        return newInstance(fragment.getContext(), fragment.getChildFragmentManager(), uat);
    }

    private static APIClientFragment newInstance(Context context, FragmentManager fragmentManager, String uat) throws InvalidArgumentException{
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }

        if (fragmentManager == null) {
            throw new IllegalArgumentException("FragmentManager can not be null.");
        }

        if (uat.isEmpty()) {
            throw new IllegalArgumentException("UAT can not be null.");
        }

        // TODO: actual impl
        // mUAT = uat;

        // TODO: test impl
        // for development
        uat = "sandbox_tmxhyf7d_dcpspy2brwdjr3qn";

        String tag = TAG_PREFIX + "." + UUID.nameUUIDFromBytes(uat.getBytes());

        if (fragmentManager.findFragmentByTag(tag) != null) {
            return (APIClientFragment) fragmentManager.findFragmentByTag(tag);
        }

        APIClientFragment apiClientFragment = new APIClientFragment();
        Bundle bundle = new Bundle();

        bundle.putString(EXTRA_UAT_STRING, uat);

        apiClientFragment.setArguments(bundle);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    fragmentManager.beginTransaction().add(apiClientFragment, tag).commitNow();
                } catch (IllegalStateException | NullPointerException e) {
                    fragmentManager.beginTransaction().add(apiClientFragment, tag).commit();
                    try {
                        fragmentManager.executePendingTransactions();
                    } catch (IllegalStateException ignored) {}
                }
            } else {
                fragmentManager.beginTransaction().add(apiClientFragment, tag).commit();
                try {
                    fragmentManager.executePendingTransactions();
                } catch (IllegalStateException ignored) {}
            }
        } catch (IllegalStateException e) {
            throw new InvalidArgumentException(e.getMessage());
        }

        apiClientFragment.mContext = context.getApplicationContext();

        return apiClientFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (mContext == null) {
            mContext = getActivity().getApplicationContext();
        }

        mUat = getArguments().getString(EXTRA_UAT_STRING);
    }

    public void checkoutWithCard(final String orderID, final CardBuilder cardBuilder) {
        if (orderID == null) {
            // TODO: handle returning error requiring value
        } else if (cardBuilder == null) {
            // TODO: handle returning error requiring value
        }

        CardBuilder newCardBuilder = new CardBuilder()
                .cardholderName("Suzie Smith")
                .cardNumber("4111111111111111")
                .expirationMonth("01")
                .expirationYear("2023")
                .cvv("123")
                .postalCode("12345");

        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_CARD_BUILDER, newCardBuilder);
        extras.putString(EXTRA_UAT_STRING, mUat);

        Intent intent = new Intent(getContext(), ApiClientActivity.class);
        intent.putExtras(extras);

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode + " resultCode: " + resultCode);
        PaymentMethodNonce nonce = data.getParcelableExtra(EXTRA_NONCE);
        Log.d(TAG, "onActivityResult: " + nonce.getNonce());
        // TODO: handle returning nonce, converting to usable value, or continuing with flow
    }
}
