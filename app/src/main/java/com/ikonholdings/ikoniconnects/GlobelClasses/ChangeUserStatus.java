package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeUserStatus extends AsyncTask<Void,Void,Void> {

    private Boolean online;

    public ChangeUserStatus(Boolean online) {
        this.online = online;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;

    }
}
