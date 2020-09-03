package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerLiveToArtist;
import com.ikonholdings.ikoniconnects.ResponseModels.CurrentLiveArtistModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CurrentLiveArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_live_to_artist,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewLiveToArtist);
        showLoadingDialogue();
        getCurrentLiveArtist();


       return v;
    }
    private void getCurrentLiveArtist(){
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<CurrentLiveArtistModel>> call = jsonApiHolder.getCurrentLiveArtist();

        call.enqueue(new Callback<List<CurrentLiveArtistModel>>() {
            @Override
            public void onResponse(Call<List<CurrentLiveArtistModel>> call, Response<List<CurrentLiveArtistModel>> response) {

                if(response.isSuccessful()){
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                    List<CurrentLiveArtistModel> liveArtistModels = response.body();
                    if(liveArtistModels.isEmpty()) {
                        //if no data then show dialoge to user
                         DialogsUtils.showAlertDialog(getContext(),false,
                                "Note","No live artist found at this moment.");
                    } else{
                       buildRecyclerView(liveArtistModels);
                    }


                }else {
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<CurrentLiveArtistModel>> call, Throwable t) {
                alertDialog.dismiss();
                mRecyclerView.setVisibility(View.VISIBLE);
                 DialogsUtils.showResponseMsg(getContext(),true);
            }
        });

    }

    private void showLoadingDialogue() {
        builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialoge_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.show();
    }

    private void buildRecyclerView(List<CurrentLiveArtistModel> liveToArtistList){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerLiveToArtist(liveToArtistList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
