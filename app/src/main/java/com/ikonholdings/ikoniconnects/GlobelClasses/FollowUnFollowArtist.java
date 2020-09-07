package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.Interfaces.FollowResultInterface;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FollowUnFollowArtist extends AsyncTask<Void,Void,Void> {
    private int CurrentStatus;
    private String artistID;
    private Context context;
    private Boolean runingFromFragment = false;//when class is runing from subcribe arits or all artist fragment

    private ProgressButton mProgressButton;

    private FollowResultInterface mInterface;

    public FollowUnFollowArtist(int CurrentStatus, String artistID, Context context, View view) {
        this.CurrentStatus = CurrentStatus;
        this.artistID = artistID;
        this.context = context;

        view.setClickable(false);
        view.setEnabled(false);

        runingFromFragment = true;

        mProgressButton = new ProgressButton(context,view);
        if(CurrentStatus == 0){
            mProgressButton.btnOnClick("Following...");
        }else {
            mProgressButton.btnOnClick("UnFollowing...");
        }
    }

    public FollowUnFollowArtist(int CurrentStatus, String artistID, Context context) {
        this.CurrentStatus = CurrentStatus;
        this.artistID = artistID;
        this.context = context;

        runingFromFragment = false;

        initializeInterface((FollowResultInterface) context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Retrofit retrofit = ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        String relativeUrl = "";
        //0 means not following yet
        if(CurrentStatus == 0){
            relativeUrl = "api/follow_artist/"+artistID;
        }else {
            relativeUrl = "api/unfollow_artist/"+artistID;
        }

        Call<SuccessErrorModel> call = jsonApiHolder.followUnFollowArtist(relativeUrl);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    if(runingFromFragment){
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(CurrentStatus == 0) {
                                    mProgressButton.btnOnCompelet("Followed");
                                } else{
                                    mProgressButton.btnOnCompelet("UnFollowed");
                                }
                            }
                        });
                    }else {
                        mInterface.followResponse(true);//sending info to DJ Acitivity
                    }
                }else {
                    if(runingFromFragment){
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogsUtils.showResponseMsg(context,
                                        false);
                            }
                        });
                    }else {
                        mInterface.followResponse(false);//sending info to DJ Acitivity
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogsUtils.showResponseMsg(context,true);
                    }
                });
            }
        });
        return null;
    }

    private void initializeInterface(FollowResultInterface Interface){
        this.mInterface = Interface;
    }

}
