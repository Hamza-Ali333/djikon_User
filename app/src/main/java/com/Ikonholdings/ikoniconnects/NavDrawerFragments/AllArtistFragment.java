package com.Ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerAllArtist;
import com.Ikonholdings.ikoniconnects.ResponseModels.AllArtistModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AllArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerAllArtist mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SearchView mSearchView;
    private List<AllArtistModel> artistList;

    private AlertDialog loadingDialog;

    @Override
    public void onStart() {
        super.onStart();
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_subscribe_artist,container,false);
        artistList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);
        mRecyclerView.setVisibility(View.GONE);
        mSearchView = v.findViewById(R.id.txt_search);

       getAllArtist();

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

    private void filter(String searchText){
        List<AllArtistModel> filteredlist = new ArrayList<>();
        String ConcatinatName;
        for(AllArtistModel item: artistList) {
            ConcatinatName = item.getFirstname()+" "+item.getLastname();
            if(ConcatinatName.toLowerCase().contains(searchText.toLowerCase())){
                filteredlist.add(item);
            }
        }

        mAdapter.filterList(filteredlist);
    }


    private void getAllArtist() {

        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<AllArtistModel>> call = jsonApiHolder.getAllArtist();

        call.enqueue(new Callback<List<AllArtistModel>>() {
            @Override
            public void onResponse(Call<List<AllArtistModel>> call, Response<List<AllArtistModel>> response) {

                if(response.isSuccessful()){

                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                    artistList = response.body();
                    if(artistList.isEmpty()) {
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "No Artist Found","it's seems like on Artist is registered Yet.!");
                    }
                    else{
                        initializeRecycler(artistList);
                        loadingDialog.dismiss();
                    }

                }else {
                    loadingDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<AllArtistModel>> call, Throwable t) {
                DialogsUtils.showResponseMsg(getContext(),true);
                loadingDialog.dismiss();
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeRecycler (List<AllArtistModel> ArtistList) {
            mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mAdapter = new RecyclerAllArtist(ArtistList,getContext());

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingDialog.show();
        getAllArtist();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyBoard.hideKeyboard(getActivity());
    }
}
