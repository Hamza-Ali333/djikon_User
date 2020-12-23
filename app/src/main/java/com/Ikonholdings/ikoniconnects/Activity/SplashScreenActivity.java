package com.Ikonholdings.ikoniconnects.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.Ikonholdings.ikoniconnects.BuildConfig;
import com.Ikonholdings.ikoniconnects.GlobelClasses.PermissionHelper;
import com.Ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.Ikonholdings.ikoniconnects.R;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //PreferenceData.clearLoginState(this);
        //mFirebaseAuth.getInstance().signOut();

       // new ChangeUserStatus(true,PreferenceData.getUserId(this)).execute();

      //  startActivity(new Intent(this,StreamingActivity.class));

        getSupportActionBar().hide();

       int i = PreferenceData.getBuildVersion(this);

       if(i != BuildConfig.VERSION_CODE) {
           PermissionHelper.managePermissions(this);
       }else{
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   if (PreferenceData.getUserLoggedInStatus(SplashScreenActivity.this)) {
                       startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                       finish();
                   }else {
                       lunchSignInActivity();
                   }
               }
           },2000);
       }
    }

    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200){
            lunchSignInActivity();
        }
    }
    private void lunchSignInActivity(){
        startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
        finish();
    }
}