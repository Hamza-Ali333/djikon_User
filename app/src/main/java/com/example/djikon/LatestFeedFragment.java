package com.example.djikon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LatestFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_latestfeed,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);






        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-161-107-128.compute-1.amazonaws.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LatestFeedJsonApi feedJsonApi = retrofit.create(LatestFeedJsonApi.class);


        Call<List<Blog_Model>> call = feedJsonApi.getBlogs();

        call.enqueue(new Callback<List<Blog_Model>>() {
            @Override
            public void onResponse(Call<List<Blog_Model>> call, Response<List<Blog_Model>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Blog_Model> blogs = response.body();
                mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new RecyclerLatestFeed(blogs,getContext());

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onFailure(Call<List<Blog_Model>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        return v;
    }
}
