package com.Ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.ResponseModels.FeedBlogModel;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.RecyclerView.RecyclerLatestFeed;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LatestFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private SwipeRefreshLayout pullToRefresh;

    private AlertDialog loadingDialog;

    private TextView txt_Msg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_latestfeed,container,false);
        createReferences(v);

        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());
        downloadBlogs();

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadBlogs();
                pullToRefresh.setRefreshing(false);
            }
        });

        return v;
    }

    private void createReferences(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
        txt_Msg = v.findViewById(R.id.info);
    }

    private void downloadBlogs() {

        Retrofit retrofit = ApiClient.retrofit(getContext());
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);
        Call<List<FeedBlogModel>> call = feedJsonApi.getBlogs();

        call.enqueue(new Callback<List<FeedBlogModel>>() {
            @Override
            public void onResponse(Call<List<FeedBlogModel>> call, Response<List<FeedBlogModel>> response) {
                if (!response.isSuccessful()) {
                    DialogsUtils.showResponseMsg(getContext(),false);
                    loadingDialog.dismiss();
                    return;
                }
                loadingDialog.dismiss();
                List<FeedBlogModel> blogs = response.body();

                if(blogs.isEmpty()){
                    txt_Msg.setVisibility(View.VISIBLE);
                }else {
                    txt_Msg.setVisibility(View.GONE);
                    mAdapter = new RecyclerLatestFeed(blogs,getContext());
                    mRecyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<FeedBlogModel>> call, Throwable t) {
                DialogsUtils.showResponseMsg(getContext(),true);
                loadingDialog.dismiss();
            }
        });
    }
}