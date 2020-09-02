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
    private List<FramesModel> listOFImagesName;
    private static final String BASEURL_IMAGES = "http://ec2-52-91-44-156.compute-1.amazonaws.com/post_images/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_social_media_share,container,false);
        mRecyclerView = v.findViewById(R.id.recyclerView_frame);

        listOFImagesName = new ArrayList<>();
        showLoadingDialogue();
        getFrames();

        return v;
    }

    private void getFrames(){

        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<FramesModel>> call = jsonApiHolder.getFrames();

        call.enqueue(new Callback<List<FramesModel>>() {
            @Override
            public void onResponse(Call<List<FramesModel>> call, Response<List<FramesModel>> response) {

                if(response.isSuccessful()){
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                    List<FramesModel> framesModelList= response.body();

                    if(framesModelList.isEmpty()) {
                        //if no data then show dialoge to user
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "Note","No live artist found at this moment.");
                    } else{

                            sperationOfArray(framesModelList);
                    }

                }else {
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);

                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<FramesModel>> call, Throwable t) {
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

    private void sperationOfArray(List<FramesModel> list){
        for (int i = 0; i < list.size() ; i++) {
            String Gallery= list.get(i).getFrame();
            Gallery = Gallery.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
            String[] GalleryArray = Gallery.split(",");

            for (int j = 0; j <= GalleryArray.length - 1; j++) {
                listOFImagesName.add(new FramesModel(BASEURL_IMAGES + GalleryArray[j]));
            }
        }

       buildRecyclerView(listOFImagesName);
    }

    private void buildRecyclerView(List<FramesModel> imageslist){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new RecyclerSocialMediaFrames(imageslist,getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
