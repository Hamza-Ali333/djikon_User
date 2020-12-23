package com.Ikonholdings.ikoniconnects.CustomDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateNewPasswordDialog {
    public static void createNewPassword(Context context,String EmailForOTP) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_update_password, null);


        ShowHidePasswordEditText Password = view.findViewById(R.id.edt_new_password);
        ShowHidePasswordEditText confirmPassword = view.findViewById(R.id.edt_ConfirmPassword);

        Button btn_update = view.findViewById(R.id.btn_changepassword);

        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_update.setEnabled(false);
                if (checkUpdatePasswordInput(Password, confirmPassword)) {
                    ProgressDialog progressDialog = DialogsUtils.showProgressDialog(context, "Updating Password", "Please Wait...");
                    Retrofit retrofit= ApiClient.retrofit(context);
                    JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
                    Call<SuccessErrorModel> call = jsonApiHolder.updatePassword(EmailForOTP,
                            Password.getText().toString().trim());

                    call.enqueue(new Callback<SuccessErrorModel>() {
                        @Override
                        public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {

                            if (response.isSuccessful()) {
                                alertDialog.dismiss();
                                progressDialog.dismiss();
                                DialogsUtils.showSuccessDialog(context,
                                        "Update SuccessFul",
                                        "You can login know with your new password",false);
                            } else {
                                progressDialog.dismiss();
                                btn_update.setEnabled(true);
                                DialogsUtils.showResponseMsg(context,
                                        false);
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                            progressDialog.dismiss();
                            DialogsUtils.showResponseMsg(context,
                                    true);
                        }
                    });
                }
            }
        });
    }

    private static boolean checkUpdatePasswordInput(EditText Password, EditText confirmPassword) {
        boolean result = true;
        if (Password.getText().toString().trim().isEmpty()) {
            Password.setError("Please Enter Your New Password");
            Password.requestFocus();
            result = false;
        } else if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Please Enter Your Password Again");
            confirmPassword.requestFocus();
            result = false;
        } else if (Password.getText().length() < 8) {
            Password.setError("Password can't be less then 8 digit");
            Password.requestFocus();
            result = false;
        } else if (!Password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
            Password.setError("Password not matched");
            Password.requestFocus();
            result = false;
        }
        return result;
    }

}
