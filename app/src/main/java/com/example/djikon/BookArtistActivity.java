package com.example.djikon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookArtistActivity extends AppCompatActivity {

    private static final String TAG ="Book this Artist" ;
    private  Button btn_Check_Cost;
    private EditText edt_Name, edt_Phone, edt_Email, edt_Address;
    private LinearLayout rlt_Start_Date, rlt_End_Date, rlt_Start_Time, rlt_End_Time;


    private TextView txt_Start_Date, txt_End_Date,txt_Start_Time, txt_End_Time;

    private String RPH ;//Rate Per Hour
    private String Name;
    private String Description;
    private String PriceType;
    private  Bitmap bitmap;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_artist);
        getSupportActionBar().setTitle("Booking Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();


        Intent intent = getIntent();
        requestCode = intent.getIntExtra("request_code",0);
        Toast.makeText(this, String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
        if(requestCode == 1) {
            RPH = intent.getStringExtra("rph");
        }else if (requestCode == 2) {
            PriceType = intent.getStringExtra("priceType");
        }
            RPH = intent.getStringExtra("price");
            Name = intent.getStringExtra("name");
            Description = intent.getStringExtra("about");
            bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");


        rlt_Start_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatPiker(txt_Start_Date);
            }
        });


        rlt_End_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatPiker(txt_End_Date);
            }
        });

        rlt_Start_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePiker(txt_Start_Time);
            }
        });


        rlt_End_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePiker(txt_End_Time);
            }
        });

        btn_Check_Cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(isInfoRight()){
                   String startDate = txt_Start_Date.getText().toString()+" "+txt_Start_Time.getText().toString() ;
                   String endTime = txt_End_Date.getText().toString()+" "+txt_End_Time.getText().toString();
                   getTimeDuration(startDate,endTime);
                   if(requestCode == 2 && !PriceType.equals("Fix")){
                       getTimeDuration(startDate,endTime);
                   }else if(requestCode == 1){
                       getTimeDuration(startDate,endTime);
                   }

               }
            }
        });
    }//onCreate





    private void showDatPiker (TextView textView) {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(BookArtistActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String _year = String.valueOf(year);
                String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String _pickedDate = _month + "/" + _date + "/" + _year;

                textView.setText(_pickedDate);
                textView.setVisibility(View.VISIBLE);
                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));

        dialog.getDatePicker().setMinDate(c.getTimeInMillis());

        dialog.show();

    }


    private void showTimePiker (TextView textView) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(BookArtistActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                textView.setText( selectedHour + ":" + selectedMinute+":00");
                textView.setVisibility(View.VISIBLE);
                if(selectedHour>12){
                    //PM
                }else {
                    //AM
                }
            }
        }, hour, minute, false);//No 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private boolean isInfoRight () {
        boolean result= true;
        if (txt_Start_Date.getText().toString().isEmpty()) {
            txt_Start_Date.setVisibility(View.VISIBLE);
            txt_Start_Date.setError("Start Date Required");
            txt_Start_Date.requestFocus();
            result = false;
        }
        else if (txt_End_Date.getText().toString().isEmpty()) {
            txt_Start_Date.setVisibility(View.VISIBLE);
            txt_Start_Date.setError("End Date Required");
            txt_Start_Date.requestFocus();
            result = false;
        } else if (txt_Start_Time.getText().toString().isEmpty()) {
            txt_Start_Time.setVisibility(View.VISIBLE);
            txt_Start_Time.setError("Start Time Required");
            txt_Start_Time.requestFocus();
            result = false;
        }else if (txt_End_Time.getText().toString().isEmpty()) {
            txt_End_Time.setVisibility(View.VISIBLE);
            txt_End_Time.setError("End Time Required");
            txt_End_Time.requestFocus();
            result = false;
        }
        else if (txt_End_Time.getText().toString().startsWith("0:")) {
            txt_End_Time.setVisibility(View.VISIBLE);
            txt_End_Time.setError("check");
            Toast.makeText(this, "Wrong Time Selected Check PM AM", Toast.LENGTH_LONG).show();
            txt_End_Time.requestFocus();
            result = false;
        }

        //EditText
        else if (edt_Name.getText().toString().isEmpty()) {
            edt_Name.setError("Name Required");
            edt_Name.requestFocus();
            result = false;
        }
        else if (edt_Name.getText().toString().length() < 3) {
            edt_Name.setError("Name is Not Valid");
            edt_Name.requestFocus();
            result = false;
        }
        else if (edt_Phone.getText().toString().isEmpty()) {
            edt_Phone.setError("Phone No Required");
            edt_Phone.requestFocus();
            result = false;
        }
        else if (edt_Email.getText().toString().isEmpty()) {
            edt_Email.setError("Email Required");
            edt_Email.requestFocus();
            result = false;
        }
        else if(!isEmailValid(edt_Email.getText().toString())){
            edt_Email.setError("Email is Not Valid");
            edt_Email.requestFocus();
            result = false;
        }
        else if(edt_Address.getText().toString().isEmpty()){
            edt_Address.setError("Address Required");
            edt_Address.requestFocus();
            result = false;
        }


        return result;
    }



    private void createRefrences () {

        btn_Check_Cost = findViewById(R.id.btn_Check_Cost);

        rlt_Start_Date = findViewById(R.id.rlt_start_Date);
        rlt_End_Date   = findViewById(R.id.rlt_end_Date);
        rlt_Start_Time = findViewById(R.id.rlt_start_time);
        rlt_End_Time   = findViewById(R.id.rlt_end_time);

        edt_Name = findViewById(R.id.edt_booker_name);
        edt_Phone = findViewById(R.id.edt_booker_phone);
        edt_Email = findViewById(R.id.edt_booker_email);
        edt_Address = findViewById(R.id.edt_booker_address);

        txt_Start_Date = findViewById(R.id.txt_start_date);
        txt_End_Date = findViewById(R.id.txt_date_end);
        txt_Start_Time = findViewById(R.id.txt_start_time);
        txt_End_Time = findViewById(R.id.txt_time_end);

    }


    //return time of the
    private void getTimeDuration(String Start, String End){

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(Start);
            d2 = format.parse(End);


            //in milliseconds
            long diff = d2.getTime() - d1.getTime();


            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            Log.i(TAG, "getTimeDuration: "+diffDays + " days, ");
            Log.i(TAG, "getTimeDuration: "+diffHours + " hours, ");
            Log.i(TAG, "getTimeDuration: "+diffMinutes + " minutes, ");

            Integer Day = (int) (long) diffDays;
            Integer Hour = (int) (long) diffHours;
            Integer Minutes = (int) (long) diffMinutes;

            if(Day != 0){
                Day = (Day * 24)*60;
            }else {
                Day = 0;
            }
            if(Hour!=0){
                Hour = Hour * 60;
            }else {
                Hour =0;
            }


            //getting total minuts
            Minutes = Day+Hour+Minutes;

            Integer ratePerHour = Integer.parseInt(RPH);

            Double perMint = Double.valueOf((double)ratePerHour / 60);

            perMint = perMint * Minutes;

            //open Dailoge after Calculation and pass the value
            openCheckCostDialogue (perMint,
                    String.valueOf(Day),
                    String.valueOf(Hour),
                    String.valueOf(Minutes) );


        } catch (Exception e) {
            Log.i(TAG, "getTimeDuration: "+e.getMessage());
        }
    }


    private void openCheckCostDialogue(Double TotalCost,String Days,String Hour,String Minutes) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.booking_cost_dialogue, null);


        CircularImageView img_Profile;
        TextView txt_Name, txt_Service_Name, txt_Servives_prize,
                                     txt_Service_Discription,
                                     txt_Service_Amount,
        //purchaser detail
         txt_pName, txt_pEmail, txt_pContact, txt_pAddress,
                //days,hour,Minutes and Rate per Hour
        txt_Days,txt_Hour,txt_Minutes,txt_RPH;

        Button btn_Cancle_Booking, btn_Book_Now;

        img_Profile = view.findViewById(R.id.img_dj_profile);

        txt_Name = view.findViewById(R.id.txt_dj_name);
        txt_Service_Name = view.findViewById(R.id.txt_service_name);

        //purchaser Detail
        txt_pName = view.findViewById(R.id.txt_pName);
        txt_pEmail = view.findViewById(R.id.txt_pEmail);
        txt_pContact = view.findViewById(R.id.txt_pPhone);
        txt_pAddress = view.findViewById(R.id.txt_pAddress);


        //Service Detail
        txt_Servives_prize = view.findViewById(R.id.txt_service_charges);
        txt_Service_Discription = view.findViewById(R.id.txt_dj_information);
        txt_Service_Amount = view.findViewById(R.id.txt_service_amount);
        //Calculated Time
        txt_Days= view.findViewById(R.id.txt_days);
        txt_Hour= view.findViewById(R.id.txt_hour);
        txt_Minutes= view.findViewById(R.id.txt_minut);
        txt_RPH= view.findViewById(R.id.txt_rph);


        btn_Cancle_Booking = view.findViewById(R.id.btn_booking_cancle);
        btn_Book_Now = view.findViewById(R.id.btn_book_now);



        img_Profile.setImageBitmap(bitmap);
        txt_Name.setText(Name);
        txt_Service_Name.setText("Book This Dj");

        //purchaser Detail
        txt_pName.setText(edt_Name.getText().toString());
        txt_pEmail.setText(edt_Email.getText().toString());
        txt_pContact.setText(edt_Phone.getText().toString());
        txt_pAddress.setText(edt_Address.getText());

        //Service Detail
        txt_Service_Discription.setText(Description);
        String.format("%.2f", TotalCost);//will remove the value after . decimal in Double
        txt_Service_Amount.setText("$"+TotalCost);
        txt_Servives_prize.setText("$"+TotalCost);

        //Calculated Time
        txt_Days.setText(Days);
        txt_Hour.setText(Hour);
        txt_Minutes.setText(Minutes);
        txt_RPH.setText("$"+RPH);

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


    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}