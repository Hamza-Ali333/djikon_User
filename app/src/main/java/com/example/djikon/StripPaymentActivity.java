package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stripe.android.PaymentConfiguration;

public class StripPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strip_payment);


        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51HCIELE5A9WQBmcX4wLziqdx2Sh9mLSg9RazyKvMn7asXrADnOK5WNjhwj7NV2NmCd8OrgFMwaCkTF7k4TmAtXVm00SCKvMjqJ"
        );

    }
}