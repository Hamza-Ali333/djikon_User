package com.Ikonholdings.ikoniconnects.GlobelClasses;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.WindowManager;

import com.Ikonholdings.ikoniconnects.R;


public class NetworkChangeReceiver extends BroadcastReceiver  {


    AlertDialog alertDialog;
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
                ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                showDialog();

            } else {
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //also checked data net is available of not
                if(alertDialog != null)
                    alertDialog.dismiss();
            }
        }
    }//onReceive

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("No Network")
                .setMessage("Internet is Required, Please Turn it on")
                .setIcon(R.drawable.ic_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        alertDialog = builder.create();


        alertDialog.show();
    }



}