package com.Ikonholdings.ikoniconnects.Activity;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.Ikonholdings.ikoniconnects.R;

public class PaymentMethodActivity extends AppCompatActivity {

    private NetworkChangeReceiver mNetworkChangeReceiver;


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmethod);
        getSupportActionBar().setTitle("Payment Methods");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}