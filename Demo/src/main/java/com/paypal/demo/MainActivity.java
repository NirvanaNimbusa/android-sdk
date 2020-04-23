package com.paypal.demo;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.api.*;
import com.paypal.api.interfaces.APIClientCallback;
import com.paypal.api.interfaces.CheckoutCompleteListener;
import com.paypal.api.models.CheckoutResult;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private APIClient ppAPIClient;
    private Button mSubmitCardButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ppAPIClient = new APIClient(this, "my-uat");

        // Set up buttons
        mSubmitCardButtom = findViewById(R.id.submitCard);
        mSubmitCardButtom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitCard:
                initiateCardCheckout();
        }
    }
    
    private void initiateCardCheckout() {
        ppAPIClient.checkoutWithCard("string", null, new APIClientCallback() {
            @Override
            public void onCheckoutComplete(CheckoutResult result) {
                Log.d("Success", "");
            }

            @Override
            public void onCheckoutFailure(Exception exception) {
                Log.d("fail", "");
            }
        });
    }

}
