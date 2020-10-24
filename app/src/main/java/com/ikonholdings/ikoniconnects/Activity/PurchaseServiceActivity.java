package com.ikonholdings.ikoniconnects.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.R;

public class PurchaseServiceActivity extends AppCompatActivity {

    private Button btn_Confirm_Payment;


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
        setContentView(R.layout.activity_purchase_service);
        getSupportActionBar().setTitle("Purchase Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        btn_Confirm_Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaymentSuccessFullDailogue();
            }
        });
    }


    private void openPaymentSuccessFullDailogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialoge_payment_successfull, null);


        TextView txt_OK = view.findViewById(R.id.txt_ok);

        builder.setView(view);
        builder.setCancelable(true);


        final AlertDialog alertDialog =  builder.show();

        txt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               Intent i = new Intent(view.getContext(),MainActivity.class);
               view.getContext().startActivity(i);

            }
        });

    }


    private void createRefrences () {
        btn_Confirm_Payment = findViewById(R.id.btn_confirm);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
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