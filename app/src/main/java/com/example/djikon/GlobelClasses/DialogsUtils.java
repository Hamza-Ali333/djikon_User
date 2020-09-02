package com.example.djikon.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.djikon.MainActivity;
import com.example.djikon.R;

public class DialogsUtils extends AppCompatDialogFragment {


    public static ProgressDialog showProgressDialog(Context context,String Title, String message){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setTitle(Title);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static AlertDialog showAlertDialog(Context context, Boolean CancelAbleState , String Title, String Msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(Title);
        builder.setMessage(Msg);
        builder.setCancelable(CancelAbleState);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_alert);

        AlertDialog alertDialog; alertDialog = builder.show();
        return alertDialog;
    }

    public static AlertDialog showResponseMsg(Context context,Boolean isFromFailed) {

        String Title, Msg;
        if(isFromFailed){
            //when Failed to connect with server
            Title = "Failed to connect with server";
            Msg = "Please check your internet.And try again";
        }else {
            //when server response is not successful
            Title = "Try Again";
            Msg = "Something happened wrong. Please try again.";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(Title);
        builder.setMessage(Msg);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_alert);

        AlertDialog alertDialog; alertDialog = builder.show();
        return alertDialog;
    }


    public static AlertDialog BookingDoneDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Congratulation");
        builder.setMessage("Your booking is successfully done." +
                "You will be inform when if DJ accept or reject Booking\nThank You");
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((Activity) context).finish();
                context.startActivity(new Intent(context,MainActivity.class));
            }
        });
        builder.setIcon(R.drawable.ic_alert);

        AlertDialog alertDialog; alertDialog = builder.show();
        return alertDialog;
    }


}
