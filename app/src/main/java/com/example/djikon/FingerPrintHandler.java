package com.example.djikon;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private Context mContext;

    public FingerPrintHandler(Context context) {
        this.mContext = context;
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

    private void update(String s, boolean b) {

        Toast.makeText(mContext, s , Toast.LENGTH_SHORT).show();
        if (b== false){
            Toast.makeText(mContext, "get an error", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(mContext,MainActivity.class);
            mContext.startActivity(intent);

        }
    }
}
