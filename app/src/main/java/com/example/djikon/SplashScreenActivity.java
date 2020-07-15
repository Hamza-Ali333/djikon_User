package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        PreferenceData preferenceData = new PreferenceData();
        getSupportActionBar().hide();
         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferenceData.getUserLoggedInStatus(SplashScreenActivity.this)){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreenActivity.this,SignInActivity.class));
                    finish();
                }

            }
        },2000);
    }
}