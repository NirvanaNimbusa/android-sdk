package com.paypal.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.CardBuilder;
import com.paypal.androidsdk.CheckoutClient;
import com.paypal.androidsdk.PayPalCheckoutListener;
import com.paypal.androidsdk.PayPalCheckoutResult;
import com.paypal.androidsdk.interfaces.CheckoutListener;
import com.paypal.androidsdk.models.CheckoutResult;
import com.paypal.demo.models.Amount;
import com.paypal.demo.models.Order;
import com.paypal.demo.models.OrderRequest;
import com.paypal.demo.models.PayPalUAT;
import com.paypal.demo.models.Payee;
import com.paypal.demo.models.PurchaseUnit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PayPalCheckoutListener {

    private CheckoutListener mCheckoutListener;
    private DemoAPIClient mDemoClient;
    private PayPalUAT mPayPalUAT;
    private String mOrderID;

    private TextView mStatusLabel;
    private TextView mUATLabel;
    private TextView mOrderIDLabel;

    private CheckoutClient checkoutClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusLabel = findViewById(R.id.statusTextView);
        mUATLabel = findViewById(R.id.uatTextView);
        mOrderIDLabel = findViewById(R.id.orderIDTextView);

        mDemoClient = RetrofitClientInstance.getInstance().create(DemoAPIClient.class);

        setUpListeners();
        fetchUAT();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkoutClient != null) {
            checkoutClient.resume(this);
        }
    }

    private Exception createUATNotFoundError() {
        return new IllegalStateException(getString(R.string.error_uat_not_found));
    }

    private void fetchUAT() {
        mUATLabel.setText(R.string.uat_fetching);

        Call<PayPalUAT> call = mDemoClient.getPayPalUAT("US");
        call.enqueue(new Callback<PayPalUAT>() {
            @Override
            public void onResponse(Call<PayPalUAT> call, Response<PayPalUAT> response) {
                Exception error = null;
                String uatString = null;

                mPayPalUAT = response.body();
                if (mPayPalUAT == null) {
                    error = createUATNotFoundError();
                } else {
                    try {
                        uatString = mPayPalUAT.getUAT();
                        if (uatString == null) {
                            error = createUATNotFoundError();
                        } else {
                            checkoutClient = new CheckoutClient(uatString, MainActivity.this);
                        }
                    } catch (InvalidArgumentException e) {
                        error = e;
                    }
                }

                if (error != null) {
                    mUATLabel.setText(getString(R.string.uat_display, error.getMessage()));
                    error.printStackTrace();
                }

                if (uatString != null) {
                    mUATLabel.setText(getString(R.string.uat_display, uatString));
                }
            }

            @Override
            public void onFailure(Call<PayPalUAT> call, Throwable e) {
                mUATLabel.setText(getString(R.string.uat_display, e.getMessage()));
            }
        });
    }

    public void fetchOrderId(View view) {
        // Construct order request
        // TODO: make this more concise
        Amount amount = new Amount();
        amount.setCurrencyCode("USD");
        amount.setValue("10.00");

        Payee payee = new Payee();
        payee.setEmailAddress("ahuang-ppcp-demo-sb1@paypal.com");

        PurchaseUnit purchaseUnit = new PurchaseUnit();
        purchaseUnit.setAmount(amount);
        purchaseUnit.setPayee(payee);

        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(purchaseUnit);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIntent("CAPTURE");
        orderRequest.setPurchaesUnits(purchaseUnits);

        // Fetch order
        mOrderIDLabel.setText(R.string.order_id_fetching);
        Call<Order> call = mDemoClient.fetchOrderID("US", orderRequest);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                Order order = response.body();
                if (order != null) {
                    mOrderID = order.getID();
                }

                if (mOrderID == null) {
                    mOrderIDLabel.setText(getString(R.string.error_order_id_not_found));
                } else {
                    mOrderIDLabel.setText(getString(R.string.order_id_display, mOrderID));
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                mOrderIDLabel.setText(getString(R.string.order_id_error));
            }
        });
    }

    private void setUpListeners() {
        mCheckoutListener = new CheckoutListener() {
            @Override
            public void onResult(@Nullable Exception error, @Nullable CheckoutResult result) {
                String status = null;
                if (result != null) {
                    status = getString(R.string.checkout_success, result.getOrderID());
                } else if (error != null) {
                    status = getString(R.string.checkout_error, error.getMessage());
                }
                mStatusLabel.setText(status);
            }
        };
    }

    // payment handler checkout implementations

    public void initiateCardCheckout(View view) {
        mStatusLabel.setText(getString(R.string.checkout_initiated));

        // trigger 3ds v1
        CardBuilder cardBuilder = new CardBuilder()
                .cardholderName("Suzie Smith")
                .cardNumber("4000000000000002")
                .expirationMonth("01")
                .expirationYear("2023")
                .cvv("123");

//        CardBuilder cardBuilder = new CardBuilder()
//                .cardholderName("Suzie Smith")
//                .cardNumber("4111111111111111")
//                .expirationMonth("01")
//                .expirationYear("2023")
//                .cvv("123");

        checkoutClient.payWithCard(cardBuilder, mOrderID, this, mCheckoutListener);
    }

    public void initiatePayPalCheckout(View view) {
        checkoutClient.payWithPayPal(mOrderID, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.reset:
                fetchUAT();
                mOrderID = null;
                mOrderIDLabel.setText(getString(R.string.order_id_placeholder));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResult(@Nullable Exception e, @Nullable PayPalCheckoutResult result) {
        // TODO: process result
    }
}
