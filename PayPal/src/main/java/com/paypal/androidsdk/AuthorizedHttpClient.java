package com.paypal.androidsdk;

import android.text.TextUtils;

import com.braintreepayments.api.BuildConfig;
import com.braintreepayments.api.exceptions.BraintreeApiErrorResponse;
import com.braintreepayments.api.exceptions.UnprocessableEntityException;
import com.braintreepayments.api.internal.HttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AuthorizedHttpClient extends HttpClient {

    private final String mBearer;

    public AuthorizedHttpClient(String baseUrl, String bearer) {
        super();

        mBaseUrl = baseUrl;
        mBearer = bearer;

        setUserAgent("braintree/android/" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected HttpURLConnection init(String url) throws IOException {
        HttpURLConnection connection = super.init(url);

        if (!TextUtils.isEmpty(mBearer)) {
            connection.setRequestProperty("Authorization", "Bearer " + mBearer);
        }
        return connection;
    }

    @Override
    protected String parseResponse(HttpURLConnection connection) throws Exception {
        try {
            return super.parseResponse(connection);
        } catch (UnprocessableEntityException e) {
            throw new BraintreeApiErrorResponse(e.getMessage());
        }
    }
}
