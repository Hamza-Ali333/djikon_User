package com.Ikonholdings.ikoniconnects.GlobelClasses;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.Ikonholdings.ikoniconnects.Activity.MainActivity;
import com.Ikonholdings.ikoniconnects.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private Context mContext;
    private TextView mTextView;
    private ImageView mImageView;

    public FingerPrintHandler(Context context, TextView textView, ImageView imageView) {
        this.mContext = context;
        this.mTextView = textView;
        this.mImageView = imageView;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate (cryptoObject, cancellationSignal,0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error"+errString,false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Update Failed",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Errorhelp "+ helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Enrolled Success Fully", true);
    }

    @SuppressLint("ResourceAsColor")
    private void update(String s, boolean b) {

        mTextView.setText(s);
        if (b == false){
            mTextView.setTextColor(Color.parseColor("#FC0000"));
            mTextView.setText(s);
        }else {
            mTextView.setTextColor(Color.parseColor("#07C02D"));
            mTextView.setText("You can proceed to next");
            mImageView.setImageResource(R.drawable.ic_check);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                }
            },1000);

        }
    }
}
