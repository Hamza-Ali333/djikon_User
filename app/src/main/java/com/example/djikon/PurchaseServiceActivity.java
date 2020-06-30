package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PurchaseServiceActivity extends AppCompatActivity {

    private Button btn_Confirm_Payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_service);
        getSupportActionBar().setTitle("Purchase Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

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
        final View view = inflater.inflate(R.layout.payment_successfull_dialoge, null);


        TextView txt_OK = view.findViewById(R.id.txt_ok);

        builder.setView(view);
        builder.setCancelable(true);


        final AlertDialog alertDialog =  builder.show();

        txt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}