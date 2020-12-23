package com.Ikonholdings.ikoniconnects.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.Ikonholdings.ikoniconnects.R;

public class BookingPaymentMethodActivity extends AppCompatActivity {

    private Button btn_PayNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_payment_method);
        getSupportActionBar().setTitle("Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        btn_PayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookingPaymentMethodActivity.this, PurchaseServiceActivity.class);
                startActivity(i);
            }
        });

    }

    private void createRefrences () {
        btn_PayNow = findViewById(R.id.btn_Pay_Now);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}