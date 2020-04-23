package com.paypal.api.models;

public class CheckoutResult {

    private String mOrderID;
    private CheckoutType mCheckoutType;

    public enum CheckoutType {
        CARD,
        PAYPAL,
        GOOGLE_PAY
    }

    public CheckoutResult(String orderID, CheckoutType checkoutType) {
        mOrderID = orderID;
        mCheckoutType = checkoutType;
    }

}
