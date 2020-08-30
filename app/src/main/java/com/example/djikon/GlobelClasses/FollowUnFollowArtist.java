package com.example.djikon.GlobelClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.ResponseModels.SuccessErrorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FollowUnFollowArtist extends AsyncTask<Void,Void,Void> {
    private int CurrentStatus;
    private String artistID;
    private AlertDialog alertDialog;
    private Context context;

    private ProgressButton mProgressButton;
    private String OnComplete;

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
            OnComplete = "Followed";
        }else {
            relativeUrl = "api/unfollow_artist/"+artistID;
            OnComplete = "UnFollowed";
        }

        Call<SuccessErrorModel> call = jsonApiHolder.followUnFollowArtist(relativeUrl);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(CurrentStatus == 0)
                            mProgressButton.btnOnDone("Followed");
                            else
                                mProgressButton.btnOnDone("UnFollowed");
                        }
                    });
                }else {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog = DialogsUtils.showResponseMsg(context,
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
                        alertDialog = DialogsUtils.showResponseMsg(context,true);
                    }
                });

            }
        });
        return null;
    }

}
