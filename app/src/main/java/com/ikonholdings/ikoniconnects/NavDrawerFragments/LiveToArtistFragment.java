package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.LiveToArtist;
import com.ikonholdings.ikoniconnects.R;

import java.util.ArrayList;


public class LiveToArtistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_live_to_artist,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerViewLiveToArtist);
        ArrayList<LiveToArtist> liveToArtistArrayList  = new ArrayList<>();
        liveToArtistArrayList.add(new LiveToArtist(R.drawable.woman,"Bilawal"));
        liveToArtistArrayList.add(new LiveToArtist(R.drawable.ic_avatar,"Hamza"));
        liveToArtistArrayList.add(new LiveToArtist(R.drawable.woman,"Usama"));
        liveToArtistArrayList.add(new LiveToArtist(R.drawable.ic_avatar,"Ahmad"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
       /// mAdapter = new RecyclerLiveToArtist(liveToArtistArrayList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


       return v;
    }
}
