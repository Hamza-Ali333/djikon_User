package com.example.djikon.GlobelClasses;

import android.app.Application;
import android.content.Context;

import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51HCIELE5A9WQBmcX4wLziqdx2Sh9mLSg9RazyKvMn7asXrADnOK5WNjhwj7NV2NmCd8OrgFMwaCkTF7k4TmAtXVm00SCKvMjqJ"
        );
    }

    }

