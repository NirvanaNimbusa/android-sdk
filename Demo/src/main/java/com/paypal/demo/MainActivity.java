package com.paypal.demo;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.androidsdk.PaymentHandler;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;
import com.paypal.androidsdk.models.CheckoutResult;
import com.paypal.demo.models.PayPalUAT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PaymentHandler mPaymentHandler;
    private CheckoutCompleteListener mCheckoutCompleteListener;
    private APIClient mDemoClient;
    private PayPalUAT mPayPalUAT;

    // UI elements
    private Button mSubmitCardButtom;
    private TextView mResultLabel;
    private TextView mUATLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemoClient = RetrofitClientInstance.getInstance().create(APIClient.class);

        fetchUAT();

        setUpListeners();

        // Set up UI elements
        mSubmitCardButtom = findViewById(R.id.submitCard);
        mSubmitCardButtom.setOnClickListener(this);
        mResultLabel = findViewById(R.id.resultTextView);
        mUATLabel = findViewById(R.id.uatTextView);
    }

    public void fetchUAT() {
        final AppCompatActivity self = this;

        Call<PayPalUAT> call = mDemoClient.getPayPalUAT("US");
        call.enqueue(new Callback<PayPalUAT>() {
            @Override
            public void onResponse(Call<PayPalUAT> call, Response<PayPalUAT> response) {
                mPayPalUAT = response.body();
                mPaymentHandler = new PaymentHandler(self, mPayPalUAT.getUAT(), mCheckoutCompleteListener);

                mUATLabel.setText("UAT: " + mPayPalUAT.getUAT());
            }

            @Override
            public void onFailure(Call<PayPalUAT> call, Throwable t) {
                Log.d(null, "Error fetching PayPal UAT.");
            }
        });
    }

    private void setUpListeners() {
        mCheckoutCompleteListener = new CheckoutCompleteListener() {
            @Override
            public void onCheckoutComplete(CheckoutResult result) {
                mResultLabel.setText("Card checkout success: " + result.getOrderID());
            }

            @Override
            public void onCheckoutError(Exception exception) {
                mResultLabel.setText("Card checkout failed.");
            }
        };
    }

    private void initiateCardCheckout() {
        mPaymentHandler.checkoutWithCard("my-order", null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitCard:
                initiateCardCheckout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
