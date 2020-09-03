package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FollowUnFollowArtist extends AsyncTask<Void,Void,Void> {
    private int CurrentStatus;
    private String artistID;
    private Context context;

    private ProgressButton mProgressButton;

    public FollowUnFollowArtist(int CurrentStatus, String artistID, Context context, View view) {
        this.CurrentStatus = CurrentStatus;
        this.artistID = artistID;
        this.context = context;

        view.setClickable(false);
        view.setEnabled(false);
        mProgressButton = new ProgressButton(context,view);
        if(CurrentStatus == 0){
            mProgressButton.btnOnClick("Following...");
        }else {
            mProgressButton.btnOnClick("UnFollowing...");
        }
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
                    Log.i("TAG", "onResponse: globel" + response.code());
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
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             DialogsUtils.showResponseMsg(context,
                                    false);
                        }
                    });
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

}
