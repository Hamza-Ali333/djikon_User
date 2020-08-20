package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.widget.Toast;

        import com.example.djikon.GlobelClasses.PreferenceData;
        import com.example.djikon.LiveStreaming.LiveVideoStreaming;
        import com.google.firebase.auth.FirebaseAuth;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;


public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        PreferenceData preferenceData = new PreferenceData();


        //preferenceData.clearPrefrences(this);
        //preferenceData.clearPrefrences(this);
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