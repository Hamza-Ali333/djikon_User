package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularimageview.CircularImageView;

public class BookArtistActivity extends AppCompatActivity {

    private  Button btn_Check_Cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_artist);
        getSupportActionBar().setTitle("Booking Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        btn_Check_Cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCheckCostDialogue();
            }
        });

    }


    private void openCheckCostDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.booking_cost_dialogue, null);

        CircularImageView img_DJ_Profile;
        TextView txt_dj_Name, txt_Service_Name, txt_Servives_prize,
                txt_Dj_Address, txt_Service_Discription,
        txt_Service_Amount, txt_Tax_Amount, txt_Paid_Amount;
        Button btn_Cancle_Booking, btn_Book_Now;

        img_DJ_Profile = view.findViewById(R.id.img_dj_profile);

        txt_dj_Name = view.findViewById(R.id.txt_dj_name);
        txt_Service_Name = view.findViewById(R.id.txt_service_name);
        txt_Dj_Address = view.findViewById(R.id.txt_dj_address);
        txt_Service_Discription = view.findViewById(R.id.txt_dj_information);
        txt_Service_Amount = view.findViewById(R.id.txt_dj_address);
        txt_Tax_Amount = view.findViewById(R.id.txt_tax_amount);
        txt_Paid_Amount = view.findViewById(R.id.txt_paid_amount);
        btn_Cancle_Booking = view.findViewById(R.id.btn_booking_cancle);
        btn_Book_Now = view.findViewById(R.id.btn_book_now);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btn_Cancle_Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_Book_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), BookingPaymentMethodActivity.class);
                view.getContext().startActivity(i);
            }
        });

    }

    private void createRefrences () {
        btn_Check_Cost = findViewById(R.id.btn_Check_Cost);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}