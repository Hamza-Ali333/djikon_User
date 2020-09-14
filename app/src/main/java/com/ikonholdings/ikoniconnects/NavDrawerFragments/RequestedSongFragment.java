package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.ResponseModels.RequestedSongsModel;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerRequestedSong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RequestedSongFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView txt_Requested_Song_Count;

    private AlertDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_requested_songs,container,false);

       Thread createRefreces = new Thread(new Runnable() {
           @Override
           public void run() {
               mRecyclerView = v.findViewById(R.id.recyclerView_song_request);
               mRecyclerView.setVisibility(View.GONE);
               txt_Requested_Song_Count = v.findViewById(R.id.txt_new_request);
           }
       });
       createRefreces.start();

       getRequestedSong();

       return v;
    }


    private void getRequestedSong() {
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
                //return all the requested song of this user
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<RequestedSongsModel>> call = jsonApiHolder.getRequestedSongs();

        call.enqueue(new Callback<List<RequestedSongsModel>>() {
            @Override
            public void onResponse(Call<List<RequestedSongsModel>> call, Response<List<RequestedSongsModel>> response) {

                if(response.isSuccessful()){

                            List<RequestedSongsModel> artistModels = response.body();
                            if(artistModels.isEmpty()){
                                //if no data then show dialoge to user
                                DialogsUtils.showAlertDialog(getContext(),false,
                                        "No Song Found","it's seems like you din't Request any Song yet");
                            }
                           else{
                               txt_Requested_Song_Count.setText("You have "+String.valueOf(artistModels.size())+" requested song.");
                                initializeRecycler(artistModels);
                            }
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                }else {
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<RequestedSongsModel>> call, Throwable t) {
                loadingDialog.dismiss();
                mRecyclerView.setVisibility(View.GONE);
                DialogsUtils.showResponseMsg(getContext(),true);
            }
        });

    }

    private void initializeRecycler (List<RequestedSongsModel> requestedSongs) {

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerRequestedSong(requestedSongs);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
