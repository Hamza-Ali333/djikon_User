package com.Ikonholdings.ikoniconnects.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.ResponseModels.AboutAndDisclouserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetAppAboutAndDisclosure extends AsyncTask<Void, Void, Void> {

    Context context;
    AlertDialog progressDialog;
    private onGetData mOnGetData;
    private boolean runingForAbout;

    public interface onGetData {
        void onGetAbout(String about);
        void onGetDisclosure(String disclosure);
    }

    private void initializeInterface(onGetData onGetData){
        this.mOnGetData = onGetData;
    }

    public GetAppAboutAndDisclosure(Context context, boolean runingForAbout) {
        this.context = context;
        this.runingForAbout = runingForAbout;
        initializeInterface((onGetData) context);
        progressDialog = DialogsUtils.showProgressDialog(context,
                "Getting...","Please Wait while fetching detail from server.");
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Retrofit retrofit = ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<AboutAndDisclouserModel> callAbout = jsonApiHolder.getAboutAndDisclosure();

        callAbout.enqueue(new Callback<AboutAndDisclouserModel>() {
            @Override
            public void onResponse(Call<AboutAndDisclouserModel> call, Response<AboutAndDisclouserModel> response) {
                AboutAndDisclouserModel data = response.body();
                if(response.isSuccessful()){
                    if (data.getAbout().isEmpty()) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogsUtils.showAlertDialog(context,
                                        false,
                                        "No About",
                                        "it's seems like Admin is not Added about yet.");
                            }
                        });
                    }
                    else if(runingForAbout) {
                       mOnGetData.onGetAbout(data.getAbout());
                    }
                    else {
                        mOnGetData.onGetDisclosure(data.getDisclosure());
                    }
                }
                 else {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogsUtils.showAlertDialog(context,
                                    false,
                                    "Error",
                                    "Please try again and check your internet connection");
                        }
                    });
                }

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call<AboutAndDisclouserModel> call, Throwable t) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogsUtils.showResponseMsg(context,true);
                        progressDialog.dismiss();
                    }
                });
            }
        });
        return null;
    }
}
