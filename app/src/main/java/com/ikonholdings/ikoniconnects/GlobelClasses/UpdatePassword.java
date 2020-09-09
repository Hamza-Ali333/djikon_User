package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.app.ProgressDialog;
import android.content.Context;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

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
                    mProgressDialog.dismiss();
                    DialogsUtils.showSuccessDialog(context,"Successful","Password is updated successfully.",false);
                }else {
                    mProgressDialog.dismiss();
                    DialogsUtils.showResponseMsg(context,false);
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                mProgressDialog.dismiss();
                DialogsUtils.showResponseMsg(context,true);
            }
        });
    }
}
