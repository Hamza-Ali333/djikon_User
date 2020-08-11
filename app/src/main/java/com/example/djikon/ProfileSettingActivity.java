package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.Models.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileSettingActivity extends AppCompatActivity {


    RelativeLayout rlt_LiveStreaming, rlt_FaceId, rlt_Biometrics, rlt_ChangePassword, rlt_ChangePin, rlt_BookingHistory,
    rlt_ConnectSocial;

    private Switch swt_FaceId_State, swt_Biometric_State;
    private Retrofit mRetrofit;
    private JSONApiHolder mJSONApiHolder;


    private static final String BASEURL ="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
    private ProgressDialog mProgressDialog;
    private AlertDialog alertDialog;

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
        setContentView(R.layout.activity_profile_setting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        rlt_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             openChangePasswordDialogue();
            }
        });

        rlt_LiveStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLiveStreaming();
            }
        });

        rlt_BookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSettingActivity.this,BookingHistoryActivity.class);
                startActivity(i);
            }
        });


        rlt_ChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePinDialogue();
            }
        });

        rlt_ConnectSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMedia();
            }
        });

    }


    private void openChangePasswordDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_change_password, null);

        EditText edt_oldPassword, edt_newPassword, edt_confirmPassword;
        Button btnResetPassword;

        edt_oldPassword = view.findViewById(R.id.edt_old_password);
        edt_newPassword = view.findViewById(R.id.edt_new_password);
        edt_confirmPassword = view.findViewById(R.id.edt_ConfirmPassword);
        btnResetPassword = view.findViewById(R.id.btn_Reset_Password);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_oldPassword.getText().toString().trim().isEmpty()){
                    edt_oldPassword.setError("Enter Old Password");
                    edt_oldPassword.requestFocus();
                }
                else if(edt_newPassword.getText().toString().trim().isEmpty()){
                    edt_newPassword.setError("Enter New Password");
                    edt_newPassword.requestFocus();
                }
                else if(edt_confirmPassword.getText().toString().trim().isEmpty()){
                    edt_confirmPassword.setError("Enter New Password");
                    edt_confirmPassword.requestFocus();
                }
                else if(!edt_newPassword.getText().toString().trim().equals(edt_confirmPassword.getText().toString().trim())){
                    edt_newPassword.setError("New Password Not Matched");
                    edt_newPassword.requestFocus();
                }
                else {
                    alertDialog.dismiss();
                    changePasswrod( edt_oldPassword.getText().toString(), edt_newPassword.getText().toString());
                    mProgressDialog = DialogsUtils.showProgressDialog(ProfileSettingActivity.this,"Updating Password","Please Wait...");
                }

            }
        });

    }

    private void openChangePinDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_change_pin, null);

        EditText edt_oldPin, edt_newPin, edt_pin;
        Button btnResetPin;

        edt_oldPin = view.findViewById(R.id.edt_old_pin);
        edt_oldPin = view.findViewById(R.id.edt_new_pin);
        edt_oldPin = view.findViewById(R.id.edt_ConfirmPin);
        btnResetPin = view.findViewById(R.id.btn_Reset_Pin);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btnResetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void openSocialMedia() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.connect_social_media, null);

       RelativeLayout rlt_Twitter, rlt_FaceBook, rlt_Instagram, rlt_Pinterst;

        rlt_Twitter = view.findViewById(R.id.connectWithTwitter);
        rlt_FaceBook = view.findViewById(R.id.connectWithFB);
        rlt_Instagram = view.findViewById(R.id.connectWithInstagram);
        rlt_Pinterst = view.findViewById(R.id.connectWithPinterst);

        builder.setView(view);
        builder.setCancelable(true);

        builder.show();
    }



    private void openLiveStreaming() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_live_streaming_option, null);

        RelativeLayout rlt_wifiOnly, rlt_CellTower, rlt_Both;


        rlt_wifiOnly = view.findViewById(R.id.rlt_wifionly);
        rlt_CellTower = view.findViewById(R.id.rlt_CellTower);
        rlt_Both = view.findViewById(R.id.rlt_Both);


        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        rlt_wifiOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "i m clicked", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }

    private void changePasswrod(String Old,String New){
        mRetrofit = ApiClient.retrofit(BASEURL,this);
        mJSONApiHolder = mRetrofit.create(JSONApiHolder.class);

        Call<SuccessErrorModel> call = mJSONApiHolder.changepasswrod( Old, New);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){

                    mProgressDialog.dismiss();
                    Toast.makeText(ProfileSettingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onResponse: "+response.code());
                }else {

                    mProgressDialog.dismiss();
                    Toast.makeText(ProfileSettingActivity.this, "Failded", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                alertDialog = DialogsUtils.showAlertDialog(ProfileSettingActivity.this,false,"No Internet","Please Check Your Internet Connection");

            }
        });
    }




    private void createRefrences(){
          rlt_ChangePassword = findViewById(R.id.rlt_ChangePassowrd);
          rlt_ChangePin = findViewById(R.id.rlt_ChangePin);
          rlt_Biometrics = findViewById(R.id.rlt_biometrics);
          rlt_BookingHistory = findViewById(R.id.rlt_bookingHistory);
          rlt_FaceId = findViewById(R.id.rlt_faceId);
          rlt_ConnectSocial = findViewById(R.id.rlt_socialmedia);
          rlt_LiveStreaming = findViewById(R.id.rlt_liveStreaming);
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