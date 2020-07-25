package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogsUtils {

    public static ProgressDialog showProgressDialog(Context context,String Title, String message){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setTitle(Title);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static AlertDialog  showAlertDailog (Context context,Boolean CancleableState ,String Title, String Msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(Title);
        builder.setMessage(Msg);
        builder.setCancelable(CancleableState);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_alert);
        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }




}
