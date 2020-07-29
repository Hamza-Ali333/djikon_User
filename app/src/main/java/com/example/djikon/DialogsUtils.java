package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogsUtils {

    AlertDialog alertDialog;
    public static ProgressDialog showProgressDialog(Context context,String Title, String message){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setTitle(Title);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static AlertDialog  showAlertDailog (Context context,Boolean CancelAbleState ,String Title, String Msg) {

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

    public  void  AlertDailogue (Context context,Boolean CancelAbleState ,String Title, String Msg) {


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

        //alertDialog =  builder.create().show();
    }

    private void dismissMyDiag() {
      //  dialog.dismiss();
    }


}
