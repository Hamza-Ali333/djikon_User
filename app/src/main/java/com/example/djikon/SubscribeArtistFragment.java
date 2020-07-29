package com.example.djikon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SubscribeArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final static String BASE_URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/following";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_subscribe_artist,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewSubscribeArtist);

        ArrayList<SubscribeToArtist> subscribeToArtistArrayList = new ArrayList<>();

        subscribeToArtistArrayList.add(new SubscribeToArtist(R.drawable.woman,"Usama","CEO Web Febricant DJ"));
        subscribeToArtistArrayList.add(new SubscribeToArtist(R.drawable.ic_doctor,"Hamza","Android Developer"));
        subscribeToArtistArrayList.add(new SubscribeToArtist(R.drawable.woman,"Usama","CEO Web Febricant DJ"));
        subscribeToArtistArrayList.add(new SubscribeToArtist(R.drawable.ic_doctor,"Ahmad","Farig Awara Wella wakeel"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerSubscribeArtist(subscribeToArtistArrayList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


       return v;
    }
}
