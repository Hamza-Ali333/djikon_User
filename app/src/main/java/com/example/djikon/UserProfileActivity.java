package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserProfileActivity extends AppCompatActivity  {


    private EditText edt_FirstName, edt_LastName, edt_Email, edt_Phone_No, edt_Location, edt_Address;

    private Button btn_Update_Profile;
    private Spinner mSpinner;


    private RelativeLayout  rlt_PaymentMethod, rlt_AboutApp, rlt_Setting ,rlt_Disclosures;
    private ConstraintLayout rlt_Parent;
    private Switch swt_subcribeState;
    private ProgressBar mProgressBar;
    private TextView msg;



    private static final String BASEURL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/user/";

    private static final String UPDATEPROFILE = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/update_profile/";

    private PreferenceData preferenceData;

    private  Retrofit retrofit;
    private  JSONApiHolder jsonApiHolder;



    private String[] genderArray = {"Select Gender","Male", "Female", "Other"};
    private String SelectedGender,
            FirstName, LastName;

    private String PhoneNo= "no";
    private String  Location = "no";
    private String  Address = "no";

    private String[] serverData;
    private String[] newData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_user_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();
        rlt_Parent.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        msg.setVisibility(View.VISIBLE);
        preferenceData= new PreferenceData();

        getUserDataFromServer();

        swt_subcribeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, R.id.genders, genderArray);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedGender = genderArray[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


        btn_Update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isInfoRight()){
                    updateProfile(preferenceData.getUserId(UserProfileActivity.this));
//                    if(isDataChange()){
//
//                        Toast.makeText(UserProfileActivity.this, "Changed Found", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(UserProfileActivity.this, "Already Updated", Toast.LENGTH_SHORT).show();
//                    }
            }

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

        retrofit = ApiResponse.retrofit(BASEURL,this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<ProfileModel> call = jsonApiHolder.getDjOrUserProfile(PreferenceData.getUserId(this));

        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if(response.isSuccessful()){
                    rlt_Parent.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    msg.setVisibility(View.GONE);

                    FirstName=response.body().getFirstname();
                    LastName=response.body().getLastname();

                    edt_Email.setText(response.body().getEmail());

                  //  Location = response.body().getLocation();
                    Address = response.body().getAddress();
                    PhoneNo = response.body().getContact();

                    setDataInToFields();

                    if(!response.body().getAddress().equals("no"))
                    edt_Address.setText(response.body().getAddress());
                }else {
                    rlt_Parent.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    msg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateProfile(String UserId){
        retrofit = ApiResponse.retrofit ( UPDATEPROFILE,this);

        jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<SuccessErrorModel> call = jsonApiHolder.UpdateUserProfile(
                UserId,
                edt_FirstName.getText().toString(),
                edt_LastName.getText().toString(),
                edt_Phone_No.getText().toString(),
                SelectedGender,
                edt_Address.getText().toString(),
                edt_Location.getText().toString());

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    Log.i("TAG", "onResponse: "+response.code());
                }else {
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isInfoRight () {
        boolean result= false;
        if (edt_FirstName.getText().toString().trim().isEmpty()) {
            edt_FirstName.setError("Required");
            edt_FirstName.requestFocus();
            result= false;
        } else if (edt_FirstName.getText().toString().trim().isEmpty()){
            edt_LastName.setError("Required");
            edt_LastName.requestFocus();
            result= false;
        }else if (!edt_Phone_No.getText().toString().trim().isEmpty() && edt_Phone_No.getText().length() < 11){
            edt_Phone_No.setError("Phone No Should be Contain 11 Digits");
            edt_Phone_No.requestFocus();
            result= false;
        }else {
            assainValue();
            result =true;
        }
        return result;
    }

    private void assainValue(){
        FirstName = edt_FirstName.getText().toString().trim();
        LastName = edt_LastName.getText().toString().trim();

        if (!edt_Phone_No.getText().toString().trim().isEmpty()){
            PhoneNo = edt_Phone_No.getText().toString().trim();
        }
        if (!edt_Address.getText().toString().trim().isEmpty()){
            Address = edt_Address.getText().toString().trim();
        }
        if (!edt_Location.getText().toString().trim().isEmpty()){
            Location = edt_Location.getText().toString().trim();
        }

        newData = new String[]{FirstName,LastName,PhoneNo,Location,SelectedGender,Address};
    }


    private boolean isDataChange(){
        boolean result= false;
        for (int i = 0; i < 6; i++) {
            if(!serverData[i].equals(newData[i])){
                serverData[i]= newData[i];
                result= true;
            }
        }
        return result;
    }


    private void setDataInToFields(){
        serverData = new String[]{FirstName, LastName, PhoneNo, Location, SelectedGender, Address};
        edt_FirstName.setText(FirstName);
        edt_LastName.setText(LastName);
        if(!PhoneNo.equals("no"))
            edt_Phone_No.setText(PhoneNo);
        if(!Location.equals("no"))
            edt_Location.setText(Location);
        if(!Address.equals("no"))
            edt_Address.setText(Address);
    }


    public void createRefrences(){

        edt_FirstName = findViewById(R.id.edt_User_First_Name);
        edt_LastName = findViewById(R.id.edt_User_LastName);
        edt_Email = findViewById(R.id.edt_UserEmail);
        edt_Phone_No = findViewById(R.id.edt_User_PhoneNo);
        edt_Location = findViewById(R.id.edt_User_Location);
        edt_Address = findViewById(R.id.edt_UserAddress);
        mSpinner = findViewById(R.id.spinner);

        btn_Update_Profile = findViewById(R.id.btn_UpdateProfile);

        swt_subcribeState = findViewById(R.id.swt_subscribeState);
        rlt_Parent = findViewById(R.id.parent);
        mProgressBar = findViewById(R.id.progress_circular);
        msg = findViewById(R.id.msg);


        rlt_AboutApp = findViewById(R.id.rlt_aboutApp);
        rlt_Disclosures = findViewById(R.id.rlt_disclosures);
        rlt_PaymentMethod = findViewById(R.id.rlt_paymentMethod);
        rlt_Setting = findViewById(R.id.rlt_setting);

    }



    }

