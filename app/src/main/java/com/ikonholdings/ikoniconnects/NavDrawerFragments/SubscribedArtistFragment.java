package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.FollowUnFollowArtist;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerSubscribedArtist;
import com.ikonholdings.ikoniconnects.ResponseModels.SubscribeArtistModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SubscribedArtistFragment extends Fragment implements FollowUnFollowArtist.FollowResultInterface {

    private RecyclerView mRecyclerView;
    private RecyclerSubscribedArtist mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AlertDialog loadingDialog;
    private SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_subscribe_artist,container,false);

         mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);
         mRecyclerView.setVisibility(View.GONE);
         mSearchView = v.findViewById(R.id.txt_search);
         mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        getSubscribedArtist();

       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               mAdapter.getFilter().filter(s);
               return false;
           }
       });

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
                    mRecyclerView.setVisibility(View.VISIBLE);

                            List<SubscribeArtistModel> artistModels = response.body();
                            if(artistModels.isEmpty()){
                               DialogsUtils.showAlertDialog(getContext(),false,
                                        "No Subscribed Artist Found","it's seems like you din't follow any artist now");
                            }
                           else
                            initializeRecycler(artistModels);

                    loadingDialog.dismiss();

                }else {
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<SubscribeArtistModel>> call, Throwable t) {
                loadingDialog.dismiss();
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

    @Override
    public void onResume() {
        super.onResume();
        getSubscribedArtist();
    }

    @Override
    public void followResponse(Boolean response) {

    }
}
