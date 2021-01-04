package com.Ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.GlobelClasses.KeyBoard;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerLiveToArtist;
import com.Ikonholdings.ikoniconnects.ResponseModels.CurrentLiveArtistModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CurrentLiveArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerLiveToArtist mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txt_Msg;

    private AlertDialog loadingDialog;
    private SearchView mSearchView;

    List<CurrentLiveArtistModel> liveArtistModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_live_to_artist,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewLiveToArtist);
        mSearchView = v.findViewById(R.id.txt_search);
        txt_Msg = v.findViewById(R.id.msg);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        liveArtistModels = new ArrayList<>();

        mSearchView = v.findViewById(R.id.txt_search);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        getCurrentLiveArtist();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

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

                   liveArtistModels = response.body();
                    if(liveArtistModels.isEmpty()) {
                        //if no data then show dialoge to user
                        txt_Msg.setVisibility(View.VISIBLE);
                    } else{
                       buildRecyclerView(liveArtistModels);
                        txt_Msg.setVisibility(View.GONE);
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

    private void filter(String searchStatus){
        List<CurrentLiveArtistModel> filteredList = new ArrayList<>();
        String searchArtistName;
        for(CurrentLiveArtistModel item: this.liveArtistModels) {
            searchArtistName = item.getFirstname()+" "+item.getLastname();
            if(searchArtistName.toLowerCase().contains(searchStatus)){
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyBoard.hideKeyboard(getActivity());
    }
}
