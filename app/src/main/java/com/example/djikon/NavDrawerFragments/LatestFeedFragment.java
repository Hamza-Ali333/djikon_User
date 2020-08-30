package com.example.djikon.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.BookArtistOrServiceActivity;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.ResponseModels.FeedBlogModel;
import com.example.djikon.R;
import com.example.djikon.RecyclerView.RecyclerLatestFeed;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


public class LatestFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private SwipeRefreshLayout pullToRefresh;
    private RelativeLayout rlt_progressBar;
    private AlertDialog alertDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_latestfeed,container,false);
        createRefrences(v);

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

    private void createRefrences (View v) {
        mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
        rlt_progressBar = v.findViewById(R.id.progressbar);
    }

    private void downloadBlogs() {

        Retrofit retrofit = ApiClient.retrofit(getContext());
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);
        Call<List<FeedBlogModel>> call = feedJsonApi.getBlogs();


        call.enqueue(new Callback<List<FeedBlogModel>>() {
            @Override
            public void onResponse(Call<List<FeedBlogModel>> call, Response<List<FeedBlogModel>> response) {

                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: "+response.code());
                    alertDialog = DialogsUtils.showResponseMsg(getContext(),false);
                    return;
                }
                        List<FeedBlogModel> blogs = response.body();

                        mAdapter = new RecyclerLatestFeed(blogs,getContext());
                        mRecyclerView.setAdapter(mAdapter);
                        rlt_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<FeedBlogModel>> call, Throwable t) {
                alertDialog = DialogsUtils.showResponseMsg(getContext(),true);
                rlt_progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
