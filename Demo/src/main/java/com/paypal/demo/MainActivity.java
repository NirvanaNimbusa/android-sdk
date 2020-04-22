package com.paypal.demo;

import androidx.appcompat.app.AppCompatActivity;
import com.paypal.api.*;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIClient ppAPIClient = new APIClient("my-favorite-uat");
    }
}
