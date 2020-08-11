package com.example.djikon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.Models.SuccessErrorModel;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookArtistOrServiceActivity extends AppCompatActivity {

    private static final String TAG = "Book this Artist";

    private final static String BASE_URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/book_artist/";
    private final static String BASE_URL_SERVICE = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/book_service/";

    private Button btn_Check_Cost;
    private RadioButton mRadioButton;
    private EditText edt_Name, edt_Phone, edt_Email, edt_Address;
    private LinearLayout rlt_Start_Date, rlt_End_Date, rlt_Start_Time, rlt_End_Time;
    private TextView txt_Start_Date, txt_End_Date, txt_Start_Time, txt_End_Time;
    private String Id;
    private String RPH;//Rate Per Hour
    private String Name;
    private String PriceType;
    private String id;
    private Bitmap bitmap;
    private int requestCode ;
    private ProgressDialog progressDialog;
    private AlertDialog MsgDialogue;

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

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
        setContentView(R.layout.activity_book_artist);
        getSupportActionBar().setTitle("Booking Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        Intent intent = getIntent();
        requestCode = intent.getIntExtra("request_code", 0);
        id = intent.getStringExtra("id");

        if (requestCode == 1) {//1 for subscriber
            RPH = intent.getStringExtra("rph");
            rlt_End_Date.setVisibility(View.VISIBLE);
            rlt_End_Time.setVisibility(View.VISIBLE);
        } else if (requestCode == 2) {
            PriceType = intent.getStringExtra("priceType");
        }

        if (requestCode == 2 && PriceType.equals("Fix")) {
            rlt_End_Date.setVisibility(View.GONE);
            rlt_End_Time.setVisibility(View.GONE);
        }

        RPH = intent.getStringExtra("price");
        Name = intent.getStringExtra("name");
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

        mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRadioButton.isSelected()) {
                    mRadioButton.setChecked(true);
                    mRadioButton.setSelected(true);
                } else {
                    mRadioButton.setChecked(false);
                    mRadioButton.setSelected(false);
                }
            }
        });

        btn_Check_Cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInfoRight()) {
                    String startDate = txt_Start_Date.getText().toString() + " " + txt_Start_Time.getText().toString();
                    String endTime = txt_End_Date.getText().toString() + " " + txt_End_Time.getText().toString();
                    getTimeDuration(startDate, endTime);
                    if (requestCode == 2 && !PriceType.equals("Fix")) {
                        getTimeDuration(startDate, endTime);
                    } else if (requestCode == 1) {
                        getTimeDuration(startDate, endTime);
                    }

                }
            }
        });
    }//onCreate

    private void showDatPiker(TextView textView) {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(BookArtistOrServiceActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String _year = String.valueOf(year);
                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String _pickedDate = _month + "/" + _date + "/" + _year;

                textView.setText(_pickedDate);
                textView.setVisibility(View.VISIBLE);
                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));

        dialog.getDatePicker().setMinDate(c.getTimeInMillis());

        dialog.show();

    }

    private void showTimePiker(TextView textView) {

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(BookArtistOrServiceActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                textView.setText(selectedHour + ":" + selectedMinute + ":00");
                textView.setVisibility(View.VISIBLE);
                if (selectedHour > 12) {
                    //PM
                } else {
                    //AM
                }
            }
        }, hour, minute, false);//No 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private boolean isInfoRight() {
        boolean result = true;
        if (txt_Start_Date.getText().toString().isEmpty()) {
            txt_Start_Date.setVisibility(View.VISIBLE);
            txt_Start_Date.setError("Start Date Required");
            txt_Start_Date.requestFocus();
            result = false;
        } else if (requestCode == 2 && !PriceType.equals("Fix") && txt_End_Date.getText().toString().isEmpty()) {
            txt_Start_Date.setVisibility(View.VISIBLE);
            txt_Start_Date.setError("End Date Required");
            txt_Start_Date.requestFocus();
            result = false;
        } else if (txt_Start_Time.getText().toString().isEmpty()) {
            txt_Start_Time.setVisibility(View.VISIBLE);
            txt_Start_Time.setError("Start Time Required");
            txt_Start_Time.requestFocus();
            result = false;
        } else if (requestCode == 2 && !PriceType.equals("Fix") && txt_End_Time.getText().toString().isEmpty()) {
            txt_End_Time.setVisibility(View.VISIBLE);
            txt_End_Time.setError("End Time Required");
            txt_End_Time.requestFocus();
            result = false;
        } else if (txt_End_Time.getText().toString().startsWith("0:")) {
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
        } else if (edt_Name.getText().toString().length() < 3) {
            edt_Name.setError("Name Should Have At Least 3 Digits");
            edt_Name.requestFocus();
            result = false;
        } else if (edt_Phone.getText().toString().isEmpty()) {
            edt_Phone.setError("Phone No Required");
            edt_Phone.requestFocus();
            result = false;
        } else if (edt_Phone.getText().toString().length() < 11) {
            edt_Phone.setError("Phone No Can't be less then 11 Digits");
            edt_Phone.requestFocus();
            result = false;
        } else if (edt_Email.getText().toString().isEmpty()) {
            edt_Email.setError("Email Required");
            edt_Email.requestFocus();
            result = false;
        } else if (!isEmailValid(edt_Email.getText().toString())) {
            edt_Email.setError("Email is Not Valid");
            edt_Email.requestFocus();
            result = false;
        } else if (edt_Address.getText().toString().isEmpty()) {
            edt_Address.setError("Address Required");
            edt_Address.requestFocus();
            result = false;
        }
        else if (!mRadioButton.isChecked()) {
            Toast.makeText(this, "You are not Agree with Terms And Condition", Toast.LENGTH_SHORT).show();
            result = false;
        }


        return result;
    }

    private void createRefrences() {

        btn_Check_Cost = findViewById(R.id.btn_Check_Cost);
        mRadioButton = findViewById(R.id.radiobtn_payment);

        rlt_Start_Date = findViewById(R.id.rlt_start_Date);
        rlt_End_Date = findViewById(R.id.rlt_end_Date);
        rlt_Start_Time = findViewById(R.id.rlt_start_time);
        rlt_End_Time = findViewById(R.id.rlt_end_time);

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
    private void getTimeDuration(String Start, String End) {

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


            Integer Day = (int) (long) diffDays;
            Integer Hour = (int) (long) diffHours;
            Integer Minutes = (int) (long) diffMinutes;

            Log.i(TAG, "getTimeDuration: " + diffDays + " days, ");
            Log.i(TAG, "getTimeDuration: " + diffHours + " hours, ");
            Log.i(TAG, "getTimeDuration: " + diffMinutes + " minutes, ");

            int TotalHour = 0;
            if (Day != 0) {
                Day = (Day * 24) * 60;
            } else {
                Day = 0;
            }
            if (Hour != 0) {
                TotalHour = Hour * 60;
            } else {
                Hour = 0;
            }


            //getting total minuts
            int TotalMinutes = Day + TotalHour + Minutes;

            Integer ratePerHour = Integer.parseInt(RPH);

            Double perMint = Double.valueOf((double) ratePerHour / 60);

            perMint = perMint * TotalMinutes;

            if (perMint > 0) {
                //open Dailoge after Calculation and pass the value
                openCheckCostDialogue(
                        perMint,//total cost
                        //converting the long into Integer
                        (int) (long) diffDays, //this is for the days
                        (int) (long) diffHours, //total Hour
                        (int) (long) diffMinutes //total minutes
                );
            } else {
                MsgDialogue = DialogsUtils.showAlertDialog(this, false,
                        "InValid Time", "Please Select the Time And Date Again With CareFully (Check PM , AM)");
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "getTimeDuration: " + e.getMessage());
        }
    }

    private void openCheckCostDialogue(Double TotalCost, int Days, int Hour, int Minutes) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_booking_cost, null);


        CircularImageView img_Profile;
        TextView txt_Name, txt_Service_Name, txt_Servives_prize,
                txt_Service_Amount,
                //purchaser detail
                txt_pName, txt_pEmail, txt_pContact, txt_pAddress,
                //days,hour,Minutes and Rate per Hour
                txt_Days, txt_Hour, txt_Minutes, txt_RPH;

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
        txt_Service_Amount = view.findViewById(R.id.txt_service_amount);
        //Calculated Time
        txt_Days = view.findViewById(R.id.txt_days);
        txt_Hour = view.findViewById(R.id.txt_hour);
        txt_Minutes = view.findViewById(R.id.txt_minut);
        txt_RPH = view.findViewById(R.id.txt_rph);


        btn_Cancle_Booking = view.findViewById(R.id.btn_booking_cancle);
        btn_Book_Now = view.findViewById(R.id.btn_book_now);


        img_Profile.setImageBitmap(bitmap);
        txt_Name.setText(Name);

        if (requestCode == 1) {
            txt_Service_Name.setText("Service Name");
        }else {
            txt_Service_Name.setText("Book This Dj");
        }
        //purchaser Detail
        txt_pName.setText(edt_Name.getText().toString());
        txt_pEmail.setText(edt_Email.getText().toString());
        txt_pContact.setText(edt_Phone.getText().toString());
        txt_pAddress.setText(edt_Address.getText());

        //Service Total Paid Amount
        txt_Service_Amount.setText("$" + new DecimalFormat("##.##").format(TotalCost));
        txt_Servives_prize.setText("$" +  new DecimalFormat("##.##").format(TotalCost));

        //Calculated Time
        txt_Days.setText(String.valueOf(Days));
        txt_Hour.setText(String.valueOf(Hour));
        txt_Minutes.setText(String.valueOf(Minutes));
        txt_RPH.setText("$" + RPH);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.show();

        btn_Cancle_Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_Book_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = DialogsUtils.showProgressDialog(v.getContext(), "Posting Request", "Please Wait");
                if(requestCode==1){
                    postBooking(String.valueOf(TotalCost),BASE_URL);
                }else {
                    postBooking(String.valueOf(TotalCost),BASE_URL_SERVICE);
                }

            }
        });

    }//openCheckCostDialogue

    private void postBooking(String PaidAmount,String BaseUrl) {

        Retrofit retrofit = ApiClient.retrofit(BaseUrl, this);

        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);


        Call<SuccessErrorModel> call = jsonApiHolder.postBooking(
                id,
                edt_Name.getText().toString(),
                edt_Email.getText().toString(),
                edt_Phone.getText().toString(),
                edt_Address.getText().toString(),
                txt_Start_Date.getText().toString(),
                txt_End_Date.getText().toString(),
                txt_Start_Time.getText().toString(),
                txt_End_Time.getText().toString(),
                PaidAmount);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent i = new Intent(BookArtistOrServiceActivity.this, BookingPaymentMethodActivity.class);
                    startActivity(i);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(BookArtistOrServiceActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                MsgDialogue = DialogsUtils.showAlertDialog(BookArtistOrServiceActivity.this,false,"No Internet","Please Check Your Internet Connection");
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}