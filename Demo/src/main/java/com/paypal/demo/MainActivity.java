package com.paypal.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.exceptions.InvalidArgumentException;
//import com.braintreepayments.browserswitch.BrowserSwitchClient;
import com.braintreepayments.browserswitch.BrowserSwitch;
import com.braintreepayments.browserswitch.BrowserSwitchClient;
import com.braintreepayments.browserswitch.BrowserSwitchListener;
import com.braintreepayments.browserswitch.BrowserSwitchResult;
import com.braintreepayments.browserswitch.IBrowserSwitchClient;
import com.paypal.androidsdk.PaymentHandler;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;
import com.paypal.androidsdk.interfaces.KanyeListener;
import com.paypal.androidsdk.models.CheckoutResult;
import com.paypal.demo.models.Amount;
import com.paypal.demo.models.Order;
import com.paypal.demo.models.OrderRequest;
import com.paypal.demo.models.PayPalUAT;
import com.paypal.demo.models.Payee;
import com.paypal.demo.models.PurchaseUnit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BrowserSwitchListener {

    // properties
    private PaymentHandler mPaymentHandler;
    private CheckoutCompleteListener mCheckoutCompleteListener;
    private DemoAPIClient mDemoClient;
    private PayPalUAT mPayPalUAT;
    private String mOrderID;

    // UI elements
    private Button mSubmitCardButton;
    private Button mOrderIDButton;
    private TextView mStatusLabel;
    private TextView mUATLabel;
    private TextView mOrderIDLabel;

    private IBrowserSwitchClient browserSwitchClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up UI elements
        mSubmitCardButton = findViewById(R.id.submitCard);
        mSubmitCardButton.setOnClickListener(this);
        mOrderIDButton = findViewById(R.id.orderIDButton);
        mOrderIDButton.setOnClickListener(this);
        mStatusLabel = findViewById(R.id.statusTextView);
        mUATLabel = findViewById(R.id.uatTextView);
        mOrderIDLabel = findViewById(R.id.orderIDTextView);

        mDemoClient = RetrofitClientInstance.getInstance().create(DemoAPIClient.class);
        browserSwitchClient = BrowserSwitch.newClient();

        setUpListeners();
        fetchUAT();
    }

    @Override
    protected void onResume() {
        super.onResume();
        browserSwitchClient.deliverResult(this);
    }

    // activity setup

    private void fetchUAT() {
        final AppCompatActivity self = this;
        mUATLabel.setText("UAT: ... fetching");

        Call<PayPalUAT> call = mDemoClient.getPayPalUAT("US");
        call.enqueue(new Callback<PayPalUAT>() {
            @Override
            public void onResponse(Call<PayPalUAT> call, Response<PayPalUAT> response) {
                mPayPalUAT = response.body();
                try {
                    mPaymentHandler = new PaymentHandler(self, mPayPalUAT.getUAT(), mCheckoutCompleteListener);
                } catch (InvalidArgumentException e) {
                    mUATLabel.setText("UAT: " + e.getMessage());
                    e.printStackTrace();
                }

                mUATLabel.setText("UAT: " + mPayPalUAT.getUAT());
            }

            @Override
            public void onFailure(Call<PayPalUAT> call, Throwable e) {
                mUATLabel.setText("UAT: " + e.getMessage());
            }
        });
    }

    private void fetchOrderID() {
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
        mOrderIDLabel.setText("Order ID: ... fetching");
        Call<Order> call = mDemoClient.fetchOrderID("US", orderRequest);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                mOrderID = response.body().getID();
                mOrderIDLabel.setText("Order ID: " + mOrderID);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                mOrderIDLabel.setText("Order ID: error" + t.getMessage());
            }
        });
    }

    private void setUpListeners() {
        mCheckoutCompleteListener = new CheckoutCompleteListener() {
            @Override
            public void onCheckoutComplete(CheckoutResult result) {
                mStatusLabel.setText("Checkout success: " + result.getOrderID());
            }

            @Override
            public void onCheckoutError(Exception e) {
                mStatusLabel.setText("Checkout failed: " + e.getMessage());
            }
        };
    }

    // payment handler checkout implementations

    private void initiateCardCheckout() {
        mStatusLabel.setText("Checking out with card ...");
        mPaymentHandler.checkoutWithCard(mOrderID, null, this, browserSwitchClient, this);
    }

    // handle UI interaction

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitCard:
                initiateCardCheckout();
                break;
            case R.id.orderIDButton:
                fetchOrderID();
                break;
        }
    }

    // menu bar

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
                mOrderIDLabel.setText("Order ID: (empty)");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBrowserSwitchResult(int requestCode, BrowserSwitchResult result, @Nullable Uri returnUri) {
        Log.d("Ye", "Yeezy");
    }
}
