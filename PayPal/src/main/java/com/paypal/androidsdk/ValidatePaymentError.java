package com.paypal.androidsdk;

import com.braintreepayments.api.exceptions.BraintreeApiErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ValidatePaymentError {

    private String contingencyUrl;

    static ValidatePaymentError from(BraintreeApiErrorResponse errorResponse) {
        return new ValidatePaymentError(errorResponse);
    }

    ValidatePaymentError(BraintreeApiErrorResponse errorResponse) {
        contingencyUrl = parse3DSContingencyUrl(errorResponse);
    }

    String getContingencyUrl() {
        return contingencyUrl;
    }

    static String parse3DSContingencyUrl(BraintreeApiErrorResponse errorResponse) {
        String responseBody = errorResponse.getErrorResponse();
        try {
            JSONObject errorJSON = new JSONObject(responseBody);
            JSONArray links = errorJSON.getJSONArray("links");
            for (int i = 0; i < links.length(); i++) {
                JSONObject obj = links.getJSONObject(i);
                String rel = obj.getString("rel");
                if (rel.equalsIgnoreCase("3ds-contingency-resolution")) {
                    return obj.getString("href");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
