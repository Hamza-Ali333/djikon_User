package com.Ikonholdings.ikoniconnects.GlobelClasses;

import android.app.ProgressDialog;
import android.content.Context;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdatePassword {
    public static void updatePassword(String Old, String New, Context context){
        ProgressDialog mProgressDialog =
                DialogsUtils.showProgressDialog(context,"Updating Password","Please Wait...");
        Retrofit retrofit= ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<SuccessErrorModel> call = jsonApiHolder.changePasswrod( Old, New);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    DialogsUtils.showAlertDialog(context,
                            false,
                            "Successful",
                            "Password is updated successfully.");
                }else if(response.code() == 404){
                    DialogsUtils.showProgressDialog(context,
                            "Error",
                            "Old password is not matched Try Again!.");
                } else {
                    DialogsUtils.showResponseMsg(context,false);
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                mProgressDialog.dismiss();
                DialogsUtils.showResponseMsg(context,true);
            }
        });
    }
}
