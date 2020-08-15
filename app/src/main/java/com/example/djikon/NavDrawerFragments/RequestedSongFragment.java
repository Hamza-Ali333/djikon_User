package com.example.djikon.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.Models.RequestedSongsModel;
import com.example.djikon.R;
import com.example.djikon.RecyclerView.RecyclerRequestedSong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RequestedSongFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final static String BASE_URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";


    private RelativeLayout progressBar;
    private AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_requested_songs,container,false);

       Thread createRefreces = new Thread(new Runnable() {
           @Override
           public void run() {
               mRecyclerView = v.findViewById(R.id.recyclerView_song_request);
               progressBar = v.findViewById(R.id.progressbar);
               mRecyclerView.setVisibility(View.GONE);
               progressBar.setVisibility(View.VISIBLE);
           }
       });
       createRefreces.start();

      getRequestedSong();

       return v;
    }


    private void getRequestedSong() {
                //return all the requested song of this user
        Retrofit retrofit= ApiClient.retrofit(BASE_URL,getContext());

        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<List<RequestedSongsModel>> call = jsonApiHolder.getRequestedSongs();

        call.enqueue(new Callback<List<RequestedSongsModel>>() {
            @Override
            public void onResponse(Call<List<RequestedSongsModel>> call, Response<List<RequestedSongsModel>> response) {

                if(response.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                            List<RequestedSongsModel> artistModels = response.body();
                            if(artistModels.isEmpty()){
                                //if no data then show dialoge to user
                                AlertDialog alertDialog = DialogsUtils.showAlertDialog(getContext(),false,
                                        "No Artist Found","it's seems like you din't Request any Song yet");
                            }
                           else
                            initializeRecycler(artistModels);

                }else {

                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();

                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RequestedSongsModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAlertDialog = DialogsUtils.showAlertDialog(getContext(),false,"No Internet","Please Check Your Internet Connection");
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