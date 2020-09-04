package com.ikonholdings.ikoniconnects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import com.ikonholdings.ikoniconnects.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.google.firebase.auth.FirebaseAuth;


public class SplashScreenActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 300;
    private static final int STORAFGE_REQUEST_CODE = 400;

    private FirebaseAuth mFirebaseAuth;
    String cameraPermission[];
    String storagePermission[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //preferenceData.clearLoginState(this);
        //mFirebaseAuth.getInstance().signOut();
        getSupportActionBar().hide();

        PermissionHelper.askForDefault(this);
    }
    private void lunchActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceData.getUserLoggedInStatus(SplashScreenActivity.this)) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                    finish();
                }
            }
        },2000);
    }

    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {

                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAFGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // pickGallary();
                    }
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        lunchActivity();
    }

}