package com.Ikonholdings.ikoniconnects.NavDrawerFragments;

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

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerLiveToArtist;
import com.Ikonholdings.ikoniconnects.ResponseModels.CurrentLiveArtistModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CurrentLiveArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AlertDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_live_to_artist,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewLiveToArtist);

        getCurrentLiveArtist();


       return v;
    }
    private void getCurrentLiveArtist(){
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<CurrentLiveArtistModel>> call = jsonApiHolder.getCurrentLiveArtist();

        call.enqueue(new Callback<List<CurrentLiveArtistModel>>() {
            @Override
            public void onResponse(Call<List<CurrentLiveArtistModel>> call, Response<List<CurrentLiveArtistModel>> response) {

                if(response.isSuccessful()){

                    List<CurrentLiveArtistModel> liveArtistModels = response.body();
                    if(liveArtistModels.isEmpty()) {
                        //if no data then show dialoge to user
                         DialogsUtils.showAlertDialog(getContext(),false,
                                "Note","No live artist found at this moment.");
                    } else{
                       buildRecyclerView(liveArtistModels);
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
            public void onFailure(Call<List<CurrentLiveArtistModel>> call, Throwable t) {
                loadingDialog.dismiss();
                mRecyclerView.setVisibility(View.GONE);
                DialogsUtils.showResponseMsg(getContext(),true);
            }
        });

    }

    private void buildRecyclerView(List<CurrentLiveArtistModel> liveToArtistList){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerLiveToArtist(liveToArtistList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
