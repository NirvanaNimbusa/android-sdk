package com.paypal.demo;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.paypal.androidsdk.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private APIClientFragment mApiClientFragment;

    // UI elements
    private Button mSubmitCardButtom;
    private TextView mResultLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mApiClientFragment = APIClientFragment.newInstance(this, "my_uat");
        } catch (InvalidArgumentException ignored ) {}

        // Set up UI
        mSubmitCardButtom = findViewById(R.id.submitCard);
        mSubmitCardButtom.setOnClickListener(this);

        mResultLabel = findViewById(R.id.resultLabel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitCard:
                initiateCardCheckout();
        }
    }

    private void initiateCardCheckout() {
        mApiClientFragment.checkoutWithCard("string", null);
    }
}
