package com.example.djikon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;


public class AlertDailogbox extends AppCompatDialogFragment {

    String Title = null;
    String Msg = null;
    Boolean CloseActivity;
    String TAG = "Alert Dialog Box";

    public AlertDailogbox(String title, String msg, Boolean closeActivity) {
        Title = title;
        Msg = msg;
        CloseActivity = closeActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Title)
                .setMessage(Msg)
                .setIcon(R.drawable.ic_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (CloseActivity == true) {
                            getActivity().finish();
                        }
                    }
                });

        return builder.create();
    }
}




