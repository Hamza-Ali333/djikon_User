package com.example.djikon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;

public class BookArtistActivity extends AppCompatActivity {

    private  Button btn_Check_Cost;
    private RelativeLayout startDate,startTime,end_Date,end_start;
    private String _pickedDate;

    private TextView txt_Start_Date,txt_Start_Time;



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

        end_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookArtistActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                showDatPiker(txt_Start_Date);
            }
        });


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookArtistActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                showDatPiker(txt_Start_Date);
            }
        });

    }//onCreate


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


    private void showDatPiker (TextView txt_view) {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(BookArtistActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String _year = String.valueOf(year);
                String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                _pickedDate = _year + "-" + _month + "-" + _date;

                txt_view.setText(_pickedDate);
                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));

        dialog.getDatePicker().setMinDate(c.getTimeInMillis());

        dialog.show();

    }



    private void createRefrences () {
        btn_Check_Cost = findViewById(R.id.btn_Check_Cost);
        startDate = findViewById(R.id.rlt_start_Date);
        startTime = findViewById(R.id.rlt_start_time);
        end_Date = findViewById(R.id.rlt_end_Date);
        txt_Start_Date = findViewById(R.id.txt_start_date);
        txt_Start_Date = findViewById(R.id.txt_start_time);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}