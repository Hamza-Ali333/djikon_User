package com.example.djikon.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.R;
import com.example.djikon.RecyclerView.RecyclerLiveToArtist;
import com.example.djikon.RecyclerView.RecyclerSocialMediaFrames;
import com.example.djikon.ResponseModels.CurrentLiveArtistModel;
import com.example.djikon.ResponseModels.FramesModel;
import com.example.djikon.ResponseModels.SliderModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SocialMediaShareFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private static final String BASEURL_IMAGES = "http://ec2-52-91-44-156.compute-1.amazonaws.com/post_images/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_social_media_share,container,false);
        mRecyclerView = v.findViewById(R.id.recyclerView_frame);
        showLoadingDialogue();
        getFrames();

        return v;
    }

    private void getFrames(){

        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<FramesModel> call = jsonApiHolder.getFrames();

        call.enqueue(new Callback<FramesModel>() {
            @Override
            public void onResponse(Call<FramesModel> call, Response<FramesModel> response) {

                if(response.isSuccessful()){
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                    String frames = response.body().getFrame();
                    Log.i("TAG", "onResponse: "+frames);
                    if(frames.isEmpty()) {
                        //if no data then show dialoge to user
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "Note","No live artist found at this moment.");
                    } else{
                        sperationOfArray(frames);
                    }

                }else {
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);

                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<FramesModel> call, Throwable t) {
                alertDialog.dismiss();
                mRecyclerView.setVisibility(View.VISIBLE);
                Log.i("TAG", "onFailure: "+t.getMessage());
//                DialogsUtils.showResponseMsg(getContext(),true);
                DialogsUtils.showAlertDialog(getContext(),false,"",t.getMessage());
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

    private void sperationOfArray(String Gallery){
        List<SliderModel> mSliderModels = new ArrayList<>();
        Gallery = Gallery.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
        String[] GalleryArray = Gallery.split(",");

        for (int i = 0; i <= GalleryArray.length - 1; i++) {
            mSliderModels.add(new SliderModel(BASEURL_IMAGES + GalleryArray[i]));
        }

       // buildRecyclerView(mSliderModels);
    }

    private void buildRecyclerView(List<SliderModel> imageslist){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerSocialMediaFrames(imageslist);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
