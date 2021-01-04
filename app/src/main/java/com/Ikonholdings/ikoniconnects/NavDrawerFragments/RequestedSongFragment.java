package com.Ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.Activity.RequestNewSongActivity;
import com.Ikonholdings.ikoniconnects.ResponseModels.RequestedSongsModel;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerRequestedSong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RequestedSongFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView txt_Requested_Song_Count, txt_Msg;

    private AlertDialog loadingDialog;

    private FloatingActionButton btn_AddSongRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_requested_songs, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerView_song_request);
        mRecyclerView.setVisibility(View.GONE);
        txt_Requested_Song_Count = v.findViewById(R.id.txt_new_request);
        txt_Msg = v.findViewById(R.id.msg);
        btn_AddSongRequest = v.findViewById(R.id.add);



        btn_AddSongRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RequestNewSongActivity.class));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRequestedSong();
    }

    private void getRequestedSong() {
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        //return all the requested song of this user
        Retrofit retrofit = ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<RequestedSongsModel>> call = jsonApiHolder.getRequestedSongs();

        call.enqueue(new Callback<List<RequestedSongsModel>>() {
            @Override
            public void onResponse(Call<List<RequestedSongsModel>> call, Response<List<RequestedSongsModel>> response) {

                if (response.isSuccessful()) {

                    List<RequestedSongsModel> artistModels = response.body();
                    if (artistModels.isEmpty()) {
                        //if no data then show dialog to user
                       txt_Msg.setVisibility(View.VISIBLE);
                    } else {
                        txt_Msg.setVisibility(View.GONE);
                        txt_Requested_Song_Count.setText("You have " + String.valueOf(artistModels.size()) + " requested song.");
                        initializeRecycler(artistModels);
                    }
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                    DialogsUtils.showResponseMsg(getContext(), false);
                }

            }

            @Override
            public void onFailure(Call<List<RequestedSongsModel>> call, Throwable t) {
                loadingDialog.dismiss();
                mRecyclerView.setVisibility(View.GONE);
                DialogsUtils.showResponseMsg(getContext(), true);
            }
        });
    }

    private void initializeRecycler(List<RequestedSongsModel> requestedSongs) {
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerRequestedSong(requestedSongs);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }




}
