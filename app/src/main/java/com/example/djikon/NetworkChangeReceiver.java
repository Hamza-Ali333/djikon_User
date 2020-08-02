package com.example.djikon;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;



import static android.content.ContentValues.TAG;


public class NetworkChangeReceiver extends BroadcastReceiver  {

    private AlertDailogBox mAlertDailogBox;
    private Context mContext;

    public NetworkChangeReceiver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );

            if (noConnectivity) {

               showMsgDailog("No Network","This is ",false);
            } else {
                if(mAlertDailogBox != null)
                    mAlertDailogBox.dismiss();
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }//onReceive

    private void showMsgDailog(String Title,String Msg, Boolean CloseActivity){
        mAlertDailogBox = new AlertDailogBox(Title,
                Msg,CloseActivity);
        mAlertDailogBox.setCancelable(false);
        try {
            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
            mAlertDailogBox.show(fragmentManager,"alert Dailog");
        } catch (ClassCastException e) {
            Log.e(TAG, "Can't get fragment manager");
        }

    }





}