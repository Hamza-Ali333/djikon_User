package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserProfileActivity extends AppCompatActivity  {


    private EditText edt_FirstName, edt_LastName, edt_Email, edt_Phone_No, edt_Location, edt_Address;

    private Button btn_Update_Profile;


    RelativeLayout  rlt_PaymentMethod, rlt_AboutApp, rlt_Setting ,rlt_Disclosures;
    Switch swt_subcribeState;

    private static final String BASEURL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/user/";

    private PreferenceData preferenceData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_user_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        preferenceData= new PreferenceData();
        getUserDataFromServer();

        swt_subcribeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
 
        rlt_PaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, PaymentMethodActivity.class);
                startActivity(i);
            }
        });

        rlt_AboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutAppDialogue();
            }
        });

        rlt_Disclosures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDisclusoreDialogue();
            }
        });

        rlt_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this,ProfileSettingActivity.class);
                startActivity(i);
            }
        });

    }



    private void openAboutAppDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_about_app, null);

        ImageView img_close = view.findViewById(R.id.close);


        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog =  builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void openDisclusoreDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_disclusore, null);

        ImageView img_close = view.findViewById(R.id.close);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog =  builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }







    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getUserDataFromServer(){
        Retrofit retrofit = ApiResponse.retrofit(BASEURL,this);
        JSONApiHolder jsonApiHolder= retrofit.create(JSONApiHolder.class);

        Call<DJProfileModel> call = jsonApiHolder.getDjOrUserProfile(PreferenceData.getUserId(this));

        call.enqueue(new Callback<DJProfileModel>() {
            @Override
            public void onResponse(Call<DJProfileModel> call, Response<DJProfileModel> response) {
                if(response.isSuccessful()){
                    edt_FirstName.setText(response.body().getFirstname());
                    edt_LastName.setText(response.body().getLastname());
                    edt_Email.setText(response.body().getEmail());
                    //edt_Phone_No.setText(response.body().get());
                   // edt_Location.setText(response.body().get());
                    edt_Address.setText(response.body().getAddress());
                }else {

                }
            }

            @Override
            public void onFailure(Call<DJProfileModel> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createRefrences(){

        edt_FirstName = findViewById(R.id.edt_User_First_Name);
        edt_LastName = findViewById(R.id.edt_User_LastName);
        edt_Email = findViewById(R.id.edt_UserEmail);
        edt_Phone_No = findViewById(R.id.edt_User_PhoneNo);
        edt_Location = findViewById(R.id.edt_User_Location);
        edt_Address = findViewById(R.id.edt_UserAddress);

        swt_subcribeState = findViewById(R.id.swt_subscribeState);
        rlt_AboutApp = findViewById(R.id.rlt_aboutApp);
        rlt_Disclosures = findViewById(R.id.rlt_disclosures);
        rlt_PaymentMethod = findViewById(R.id.rlt_paymentMethod);
        rlt_Setting = findViewById(R.id.rlt_setting);

    }



    }

