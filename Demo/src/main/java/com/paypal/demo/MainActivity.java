package com.paypal.demo;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.models.PaymentMethodNonce;
import com.paypal.androidsdk.*;
import com.paypal.androidsdk.interfaces.CheckoutCompleteListener;
import com.paypal.androidsdk.models.CheckoutResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private APIClient ppAPIClient;
    private CheckoutCompleteListener mCheckoutCompleteListener;

    // UI elements
    private Button mSubmitCardButtom;
    private TextView mResultLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpListeners();
        ppAPIClient = new APIClient(this, "my-uat", mCheckoutCompleteListener);

        // Set up UI
        mSubmitCardButtom = findViewById(R.id.submitCard);
        mSubmitCardButtom.setOnClickListener(this);

        mResultLabel = findViewById(R.id.resultLabel);
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
        ppAPIClient.checkoutWithCard("string", null);
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
