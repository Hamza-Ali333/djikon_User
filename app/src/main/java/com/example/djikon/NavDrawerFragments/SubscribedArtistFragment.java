package com.example.djikon.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.BookArtistOrServiceActivity;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.ResponseModels.SubscribeArtistModel;
import com.example.djikon.R;
import com.example.djikon.RecyclerView.RecyclerSubscribedArtist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SubscribedArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RelativeLayout progressBar;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_subscribe_artist,container,false);

       Thread createRefreces = new Thread(new Runnable() {
           @Override
           public void run() {
               mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);
               progressBar = v.findViewById(R.id.progressbar);
               mRecyclerView.setVisibility(View.GONE);
               progressBar.setVisibility(View.VISIBLE);
           }
       });
       createRefreces.start();

       getSubscribedArtist();

       return v;
    }


    private void getSubscribedArtist () {
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<SubscribeArtistModel>> call = jsonApiHolder.getSubscribeArtist();

        call.enqueue(new Callback<List<SubscribeArtistModel>>() {
            @Override
            public void onResponse(Call<List<SubscribeArtistModel>> call, Response<List<SubscribeArtistModel>> response) {

                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                            List<SubscribeArtistModel> artistModels = response.body();
                            if(artistModels.isEmpty()){
                                AlertDialog alertDialog = DialogsUtils.showAlertDialog(getContext(),false,
                                        "No Subscribed Artist Found","it's seems like you din't follow any artist now");
                            }
                           else
                            initializeRecycler(artistModels);

                }else {
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    alertDialog = DialogsUtils.showResponseMsg(getContext(),false);

                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SubscribeArtistModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                DialogsUtils.showResponseMsg(getContext(),true);
            }
        });
    }

    private void initializeRecycler (List<SubscribeArtistModel> ArtistList) {

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerSubscribedArtist(ArtistList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }



}
