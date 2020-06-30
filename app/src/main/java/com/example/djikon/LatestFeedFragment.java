package com.example.djikon;

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

import java.util.ArrayList;


public class LatestFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_latestfeed,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewLatestFeed);

        ArrayList<LatestFeedItem> latestFeedItemArrayList = new ArrayList<>();
        latestFeedItemArrayList.add(new LatestFeedItem(R.drawable.ic_doctor,R.drawable.rectangle,"Hamza","2m ago","you will enjoye the event","1","23"));
        latestFeedItemArrayList.add(new LatestFeedItem(R.drawable.woman,R.drawable.rectangle2,"Ahmad","6m ago","you will enjoye the event","5","3"));
        latestFeedItemArrayList.add(new LatestFeedItem(R.drawable.ic_doctor,R.drawable.rectangle,"Bilawal","6m ago","you will enjoye the event","8","23"));
        latestFeedItemArrayList.add(new LatestFeedItem(R.drawable.woman,R.drawable.rectangle2,"Usama","7m ago","you will enjoye the event","90","34"));

            mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mAdapter = new RecyclerLatestFeed(latestFeedItemArrayList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

        return v;
    }
}
