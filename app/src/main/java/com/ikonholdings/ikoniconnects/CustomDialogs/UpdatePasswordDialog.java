package com.ikonholdings.ikoniconnects.CustomDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ikonholdings.ikoniconnects.GlobelClasses.UpdatePassword;
import com.ikonholdings.ikoniconnects.R;


public class UpdatePasswordDialog {

    public static void showChangePasswordDialogue(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_change_password, null);

        EditText edt_oldPassword, edt_newPassword, edt_confirmPassword;
        Button btnResetPassword;

        edt_oldPassword = view.findViewById(R.id.edt_old_password);
        edt_newPassword = view.findViewById(R.id.edt_new_password);
        edt_confirmPassword = view.findViewById(R.id.edt_ConfirmPassword);
        btnResetPassword = view.findViewById(R.id.btn_Reset_Password);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog =  builder.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_oldPassword.getText().toString().trim().isEmpty()){
                    edt_oldPassword.setError("Enter Old Password");
                    edt_oldPassword.requestFocus();
                }
                else if(edt_newPassword.getText().toString().trim().isEmpty()){
                    edt_newPassword.setError("Enter New Password");
                    edt_newPassword.requestFocus();
                }
                else if(edt_confirmPassword.getText().toString().trim().isEmpty()){
                    edt_confirmPassword.setError("Enter New Password");
                    edt_confirmPassword.requestFocus();
                }
                else if(!edt_newPassword.getText().toString().trim().equals(edt_confirmPassword.getText().toString().trim())){
                    edt_newPassword.setError("New Password Not Matched");
                    edt_newPassword.requestFocus();
                }
                else {
                    alertDialog.dismiss();

                    UpdatePassword.updatePassword(edt_oldPassword.getText().toString(),
                            edt_newPassword.getText().toString(),
                            context);
                }

            }
        });
    }

}
