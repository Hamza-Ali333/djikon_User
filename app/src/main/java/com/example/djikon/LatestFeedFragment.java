package com.example.djikon;

import android.app.AlertDialog;
import android.os.AsyncTask;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class LatestFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private SwipeRefreshLayout pullToRefresh;
    private RelativeLayout rlt_progressBar;
    private AlertDialog alertDialog;

    private static final String BASE_URL="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_latestfeed,container,false);
        createRefrences(v);

        downloadBlogs();
       //new background().execute();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlt_progressBar.setVisibility(View.VISIBLE);
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

        Retrofit retrofit = ApiClient.retrofit(BASE_URL,getContext());

        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

        Call<List<FeedBlogModel>> call = feedJsonApi.getBlogs();


        call.enqueue(new Callback<List<FeedBlogModel>>() {
            @Override
            public void onResponse(Call<List<FeedBlogModel>> call, Response<List<FeedBlogModel>> response) {

                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: "+response.code());
                    return;
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<FeedBlogModel> blogs = response.body();
                        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
                        mLayoutManager = new LinearLayoutManager(getContext());
                        mAdapter = new RecyclerLatestFeed(blogs,getContext());


                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);

                    }
                });
                thread.start();

                rlt_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<FeedBlogModel>> call, Throwable t) {
                alertDialog = DialogsUtils.showAlertDialog(getContext(),false,"No Internet","Please Check Your Internet Connection");
                rlt_progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    private class background extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://ec2-54-161-107-128.compute-1.amazonaws.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

            Call<List<FeedBlogModel>> call = feedJsonApi.getBlogs();


            call.enqueue(new Callback<List<FeedBlogModel>>() {
                @Override
                public void onResponse(Call<List<FeedBlogModel>> call, Response<List<FeedBlogModel>> response) {
                    if (!response.isSuccessful()) {
                        Log.i(TAG, "onResponse: "+response.code());
                        return;
                    }

                    List<FeedBlogModel> blogs = response.body();
                    mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new RecyclerLatestFeed(blogs,getContext());

                    rlt_progressBar.setVisibility(View.GONE);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onFailure(Call<List<FeedBlogModel>> call, Throwable t) {
                    Log.i(TAG, "onFailure: "+t.getMessage());

                }
            });

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            rlt_progressBar.setVisibility(View.INVISIBLE);
        }


    }


}
