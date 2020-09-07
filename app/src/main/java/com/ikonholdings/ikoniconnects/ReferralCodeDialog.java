package com.ikonholdings.ikoniconnects;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.ResponseModels.LoginRegistrationModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import java.lang.ref.PhantomReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReferralCodeDialog {

    public static AlertDialog showReferralCodeDialog(Context context, String Email) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dailoge_referal_code, null);

        Button btn_update = view.findViewById(R.id.btn_changepassword);
        EditText edt_Code = view.findViewById(R.id.edt_refrel_code);

        builder.setView(view);
        builder.setCancelable(false);

        Retrofit  retrofit= ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        final AlertDialog alertDialog = builder.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_Code.getText() == null || edt_Code.getText().toString().length() == 0){

                    Toast.makeText(context, "Please Enter Referral Code", Toast.LENGTH_SHORT).show();
                }else {
                    ProgressDialog progressDialog = DialogsUtils.showProgressDialog(context,"Checking Code",
                            "Please wait while server is checking validation of code.");
                    Call<LoginRegistrationModel> call = jsonApiHolder.postReferral(edt_Code.getText().toString(), Email);

                    call.enqueue(new Callback<LoginRegistrationModel>() {
                        @Override
                        public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                            if(response.isSuccessful()){
                                progressDialog.dismiss();
                                LoginRegistrationModel data = response.body();
                                saveDataInPreferences(context,
                                        data.getSuccess(),
                                        String.valueOf(data.getId()),
                                        data.getFirstname()+" "+data.getLastname(),
                                        data.getProfile_image(),
                                        Email,
                                        "K283K8CoZBqp");
                                context.startActivity(new Intent(context,MainActivity.class));
                            }else if(response.code() == 400) {
                                progressDialog.dismiss();
                                edt_Code.setError("Wrong Code");
                                edt_Code.requestFocus();
                            }
                            else {
                                progressDialog.dismiss();
                                DialogsUtils.showResponseMsg(context,false);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                            progressDialog.dismiss();
                            DialogsUtils.showResponseMsg(context,true);
                        }
                    });
                }
            }
        });
        return alertDialog;
    }



    private static void saveDataInPreferences(Context context,
                                              String userToken, String userId, String userName, String profileImage, String userEmail, String userPassword){
        PreferenceData.setUserToken(context, userToken);
        PreferenceData.setUserId(context, userId);
        PreferenceData.setUserName(context, userName);
        PreferenceData.setUserImage(context, profileImage);
        PreferenceData.setUserLoggedInStatus(context, true);
        PreferenceData.setUserEmail(context, userEmail);
        PreferenceData.setUserPassword(context, userPassword);
    }
}

