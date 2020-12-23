package com.Ikonholdings.ikoniconnects.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.Ikonholdings.ikoniconnects.Activity.MainActivity;
import com.Ikonholdings.ikoniconnects.R;

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

    public static AlertDialog showLoadingDialogue(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoge_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alertDialog; alertDialog = builder.show();
        return alertDialog;
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

    public static AlertDialog showSuccessDialog(Context context, String Title, String Msg,Boolean isBookingActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(Title);
        builder.setMessage(Msg);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(isBookingActivity){
                    ((Activity) context).finish();
                    context.startActivity(new Intent(context,MainActivity.class));
                }
            }
        });
        builder.setIcon(R.drawable.ic_check);

        AlertDialog alertDialog; alertDialog = builder.show();
        return alertDialog;
    }
}
