package com.ikonholdings.ikoniconnects.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ikonholdings.ikoniconnects.CustomDialogs.CreateNewPasswordDialog;
import com.ikonholdings.ikoniconnects.CustomDialogs.UpdatePasswordDialog;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.R;

public class ProfileSettingActivity extends AppCompatActivity {

    RelativeLayout rlt_LiveStreaming, rlt_Biometrics, rlt_ChangePassword, rlt_ChangePin,
    rlt_ConnectSocial;

    private Switch swt_FaceId_State, swt_Biometric_State;

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

        Intent info = getIntent();
        Boolean isHavePassword = info.getBooleanExtra("password",false);

        if(PreferenceData.getBiometricLoginState(ProfileSettingActivity.this)){
            swt_Biometric_State.setChecked(true);
        }else {
            swt_Biometric_State.setChecked(false);
        }

        rlt_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(isHavePassword){
                 UpdatePasswordDialog.showChangePasswordDialogue(ProfileSettingActivity.this);
             }else {
                 //openCreate
                 CreateNewPasswordDialog.createNewPassword(ProfileSettingActivity.this,
                         PreferenceData.getUserEmail(ProfileSettingActivity.this));
             }
            }
        });

        rlt_LiveStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLiveStreaming();
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

        swt_Biometric_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swt_Biometric_State.isChecked()) {
                     DialogsUtils.showAlertDialog(ProfileSettingActivity.this,
                            false,
                            "Note","Biometric is Enable Now");
                    PreferenceData.setBiometricLoginState(ProfileSettingActivity.this,true);
                }else {
                    DialogsUtils.showAlertDialog(ProfileSettingActivity.this,
                            false,
                            "Note","Biometric is Disable Now");
                    PreferenceData.setBiometricLoginState(ProfileSettingActivity.this,false);
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

        final AlertDialog alertDialog =  builder.show();

        btnResetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "this feature will available soon", Toast.LENGTH_SHORT).show();
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

        rlt_Twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "this feature will available soon", Toast.LENGTH_SHORT).show();
            }
        });

        rlt_FaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "this feature will available soon", Toast.LENGTH_SHORT).show();
            }
        });

        rlt_Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "this feature will available soon", Toast.LENGTH_SHORT).show();
            }
        });

        rlt_Pinterst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileSettingActivity.this, "this feature will available soon", Toast.LENGTH_SHORT).show();
            }
        });

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
                Toast.makeText(ProfileSettingActivity.this, "this feature will available soon", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

    }

    private void createRefrences(){
          rlt_ChangePassword = findViewById(R.id.rlt_ChangePassowrd);
          rlt_ChangePin = findViewById(R.id.rlt_ChangePin);
          rlt_Biometrics = findViewById(R.id.rlt_biometrics);
          rlt_ConnectSocial = findViewById(R.id.rlt_socialmedia);
          rlt_LiveStreaming = findViewById(R.id.rlt_liveStreaming);

         swt_Biometric_State = findViewById(R.id.swt_biometric_state);
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