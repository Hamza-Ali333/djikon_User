package com.ikonholdings.ikoniconnects.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookArtistOrServiceActivity extends AppCompatActivity {

    private Button btn_Check_Cost;
    private RadioButton mRadioButton;
    private EditText edt_Name, edt_Phone, edt_Email, edt_Address;
    private LinearLayout rlt_Start_Date, rlt_End_Date, rlt_Start_Time, rlt_End_Time;
    private TextView txt_Start_Date, txt_End_Date, txt_Start_Time, txt_End_Time;

    //intent extra data will stored in these veriables
    private Boolean bookingForArtist;
    private Boolean bookingForService;
    private String RPH;//Rate Per Hour
    private String serviceOrSubscriberName;
    private String priceType;
    private int artistId;//artist id
    private int serviceId;
    private String TotalAmount;
    private Bitmap bitmap;
    private String description;

    private ProgressDialog progressDialog;
    private AlertDialog checkBookingCoastDialog;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;
    private String BrainTreeToken;
    private static final int DROP_IN_REQUEST_CODE = 777;

    private Context context;//context for asynkTask

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
        createReferences();//Front End View to Back End

        retrofit = ApiClient.retrofit(this);
        context = this;

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        Intent intent = getIntent();

        artistId = intent.getIntExtra("artistId", 0);
        bookingForArtist = intent.getBooleanExtra("bookingForArtist", false);
        serviceId = intent.getIntExtra("serviceId", 0);
        bookingForArtist = intent.getBooleanExtra("bookingForArtist", false);
        bookingForService = intent.getBooleanExtra("bookingForService", false);
        priceType = intent.getStringExtra("priceType");
        bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        RPH = intent.getStringExtra("price");
        serviceOrSubscriberName = intent.getStringExtra("serviceOrSubscriberName");
        description = intent.getStringExtra("description");

        setPreferencesDataIntoViews();//set User information into EditText which we already have

        if (bookingForArtist == true && bookingForService == false) {//if ture
            rlt_End_Date.setVisibility(View.VISIBLE);
            rlt_End_Time.setVisibility(View.VISIBLE);
        }

        //if booking for service and price type is Fix then hide extra views
        if (bookingForArtist == false && bookingForService == true && priceType.equals("Fix")) {
            rlt_End_Date.setVisibility(View.GONE);
            rlt_End_Time.setVisibility(View.GONE);
        }

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
                    if (!priceType.equals("Fix")) {
                        String startDate = txt_Start_Date.getText().toString() + " " + txt_Start_Time.getText().toString();
                        String endTime = txt_End_Date.getText().toString() + " " + txt_End_Time.getText().toString();
                        getTimeDuration(startDate, endTime);
                    } else {
                        //no nead to get time Duration
                        openCheckCostDialogue(Double.valueOf(RPH), 0, 0, 0);//when user is booking fix service
                    }
                }
            }
        });
    }//onCreate

    private void setPreferencesDataIntoViews() {
        edt_Name.setText(PreferenceData.getUserName(this));
        edt_Email.setText(PreferenceData.getUserEmail(this));

        if (!PreferenceData.getUserAddress(this).equals("no"))
            edt_Address.setText(PreferenceData.getUserAddress(this));

        if (!PreferenceData.getUserPhoneNo(this).equals("no"))
            edt_Phone.setText(PreferenceData.getUserPhoneNo(this));


    }

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
        } else if (bookingForArtist && !priceType.equals("Fix") && txt_End_Date.getText().toString().isEmpty()) {
            txt_Start_Date.setVisibility(View.VISIBLE);
            txt_Start_Date.setError("End Date Required");
            txt_Start_Date.requestFocus();
            result = false;
        } else if (txt_Start_Time.getText().toString().isEmpty()) {
            txt_Start_Time.setVisibility(View.VISIBLE);
            txt_Start_Time.setError("Start Time Required");
            txt_Start_Time.requestFocus();
            result = false;
        } else if (bookingForArtist && !priceType.equals("Fix") && txt_End_Time.getText().toString().isEmpty()) {
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
        } else if (!mRadioButton.isChecked()) {
            Toast.makeText(this, "You are not Agree with Terms And Condition", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private void createReferences() {

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

        txt_Start_Date = findViewById(R.id.start_date);
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
               DialogsUtils.showAlertDialog(this, false,
                        "InValid Time", "Please Select the Time And Date Again With CareFully (Check PM , AM)");
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("TAG", "getTimeDuration: " + e.getMessage());
        }
    }

    private void openCheckCostDialogue(Double TotalCost, int Days, int Hour, int Minutes) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_booking_cost, null);

        CircularImageView img_Profile;
        TextView RateTypeTitle;
        TextView txt_Name, txt_Service_Name, txt_Servives_prize,
                txt_Service_Amount,
                //purchaser detail
                txt_pName, txt_pEmail, txt_pContact, txt_pAddress,
                //days,hour,Minutes and Rate per Hour
                txt_Days, txt_Hour, txt_Minutes, txt_RPH,
                //Time And Date
                start_date, start_time, end_date, end_time;


        Button btn_Cancle_Booking, btn_Book_Now;

        img_Profile = view.findViewById(R.id.img_subscriber_profile);

        RateTypeTitle = view.findViewById(R.id.txt_rate_per_hour);

        txt_Name = view.findViewById(R.id.txt_subscriber_name);
        txt_Service_Name = view.findViewById(R.id.txt_service_name);

        //purchaser Detail
        txt_pName = view.findViewById(R.id.txt_pName);
        txt_pEmail = view.findViewById(R.id.txt_pEmail);
        txt_pContact = view.findViewById(R.id.txt_pPhone);
        txt_pAddress = view.findViewById(R.id.txt_pAddress);

        //Time And Date references
        start_date = view.findViewById(R.id.start_date);
        start_time = view.findViewById(R.id.start_time);
        end_date = view.findViewById(R.id.end_date);
        end_time = view.findViewById(R.id.end_time);


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
        txt_Name.setText(serviceOrSubscriberName);

        txt_Service_Name.setText(serviceOrSubscriberName);
        //purchaser Detail
        txt_pName.setText(edt_Name.getText().toString());
        txt_pEmail.setText(edt_Email.getText().toString());
        txt_pContact.setText(edt_Phone.getText().toString());
        txt_pAddress.setText(edt_Address.getText());

        //setting Date And Time
        if (!priceType.equals("Fix")) {
            start_date.setText(txt_Start_Date.getText().toString());
            start_time.setText(txt_Start_Time.getText().toString());
            end_date.setText(txt_End_Date.getText().toString());
            end_time.setText(txt_End_Time.getText().toString());
        } else {
            RateTypeTitle.setText("Fix Rate");
            start_date.setText(txt_Start_Date.getText().toString());
            start_time.setText(txt_Start_Time.getText().toString());
            end_date.setVisibility(View.GONE);
            end_time.setVisibility(View.GONE);
            TextView time = view.findViewById(R.id.time);
            TextView date = view.findViewById(R.id.date);
            time.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
        }

        //Service Total Paid Amount
        txt_Service_Amount.setText("$" + new DecimalFormat("##.##").format(TotalCost));
        txt_Servives_prize.setText("$" + new DecimalFormat("##.##").format(TotalCost));

        //Calculated Time
        txt_Days.setText(String.valueOf(Days));
        txt_Hour.setText(String.valueOf(Hour));
        txt_Minutes.setText(String.valueOf(Minutes));
        txt_RPH.setText("$" + RPH);

        builder.setView(view);
        builder.setCancelable(false);

        checkBookingCoastDialog = builder.show();

        btn_Cancle_Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBookingCoastDialog.dismiss();
            }
        });

        btn_Book_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBookingCoastDialog.dismiss();
                TotalAmount = String.valueOf(TotalCost);//for sending amount with nonce to server
                new GetBrainTreeToken().execute();
            }
        });

    }//openCheckCostDialogue

    private void postBooking(String PaidAmount) {
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        String EndDate, EndTime;

        if (priceType.equals("Fix")) {
            EndDate = "0";
            EndTime = "0";
        }else {
            EndDate = txt_End_Date.getText().toString();
            EndTime = txt_End_Time.getText().toString();
        }

        Call<SuccessErrorModel> call = jsonApiHolder.postBooking(
                artistId,
                serviceId,
                edt_Name.getText().toString(),
                edt_Email.getText().toString(),
                edt_Phone.getText().toString(),
                edt_Address.getText().toString(),
                txt_Start_Date.getText().toString(),
                EndDate,
                txt_Start_Time.getText().toString(),
                EndTime,
                PaidAmount);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            DialogsUtils.showSuccessDialog(BookArtistOrServiceActivity.this,"Congratulation","Your booking is successfully done." +
                                    "You will be inform when if Subscriber accept or reject Booking\nThank You",true);
                        } else {
                            progressDialog.dismiss();
                            DialogsUtils.showResponseMsg(BookArtistOrServiceActivity.this, false);
                            Log.i("TAG", "onResponse: " + response.code());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        DialogsUtils.showResponseMsg(BookArtistOrServiceActivity.this, true);
                    }
                });
            }
        });
    }

    public void onBrainTreeSubmit(String braintreetoken) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(braintreetoken);

        startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DROP_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                progressDialog = DialogsUtils.showProgressDialog(context, "Post Amount", "Please wait while confirming collecting amount of service");

                //PostNonceToServer(paymentMethodNonce);
                postNonceWithRetrofit(paymentMethodNonce);
            } else if (resultCode == RESULT_CANCELED) {
                //the user canceled
                Toast.makeText(this, "Payment Cancle", Toast.LENGTH_SHORT).show();
            } else {
                // handle errors here, an exception may be available in
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        } else {
            Toast.makeText(this, "Card not found.Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetBrainTreeToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(context, "Posting Request", "Please Wait");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SuccessErrorModel> call = jsonApiHolder.getBrainTreeToken();

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if (response.isSuccessful()) {
                        SuccessErrorModel successErrorModel = response.body();
                        BrainTreeToken = successErrorModel.getSuccess();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                checkBookingCoastDialog.dismiss();
                                onBrainTreeSubmit(BrainTreeToken);
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        DialogsUtils.showResponseMsg(BookArtistOrServiceActivity.this, false);
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            DialogsUtils.showResponseMsg(BookArtistOrServiceActivity.this, true);
                        }
                    });
                }
            });
            return null;
        }
    }


    private void postNonceWithRetrofit(String Nonce){
            String EndDate, EndTime;

            if (priceType.equals("Fix")) {
                EndDate = "0";
                EndTime = "0";
            }else {
                EndDate = txt_End_Date.getText().toString();
                EndTime = txt_End_Time.getText().toString();
            }

        Map<String, String> params = new HashMap<>();

            params.put("payment_method_nonce", Nonce);
            params.put("amount", TotalAmount);
            params.put("receiver_id", String.valueOf(artistId));
            params.put("sub_id", String.valueOf(artistId));
            params.put("sender_id", PreferenceData.getUserId(this));
            params.put("service_id", String.valueOf(serviceId));
            params.put("name", edt_Name.getText().toString());
            params.put("email",  edt_Email.getText().toString());
            params.put("phone",  edt_Phone.getText().toString());
            params.put("address",  edt_Address.getText().toString());
            params.put("start_date",  txt_Start_Date.getText().toString());
            params.put("end_date", EndDate);
            params.put("start_time",  txt_Start_Time.getText().toString());
            params.put("end_time", EndTime);

        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<Void> call = jsonApiHolder.Checkout(params);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    DialogsUtils.showSuccessDialog(BookArtistOrServiceActivity.this,"Congratulation","Your booking is successfully done." +
                            "You will be inform when if Subscriber accept or reject Booking\nThank You",true);

                }else if(response.code() == 404){
                    progressDialog.dismiss();
                    DialogsUtils.showAlertDialog(BookArtistOrServiceActivity.this,
                            false, "Error",
                            "Something went wrong with your card");
                }
                else {
                    progressDialog.dismiss();
                    DialogsUtils.showResponseMsg(BookArtistOrServiceActivity.this, false);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                DialogsUtils.showAlertDialog(BookArtistOrServiceActivity.this, false, " Error", t.getMessage());
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
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}