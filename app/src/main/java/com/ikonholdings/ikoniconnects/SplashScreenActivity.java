package com.ikonholdings.ikoniconnects;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
        import android.os.Handler;

import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.google.firebase.auth.FirebaseAuth;


public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        PreferenceData preferenceData = new PreferenceData();

        //preferenceData.clearLoginState(this);
        //mFirebaseAuth.getInstance().signOut();

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferenceData.getUserLoggedInStatus(SplashScreenActivity.this)) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                    finish();
                }
            }
        },2000);
    }
}