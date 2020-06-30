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


public class ChatAreaFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_area,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerView_Chat);

        ArrayList<Chat_Model> chat_modelArrayList = new ArrayList<>();

        chat_modelArrayList.add(new Chat_Model(R.drawable.woman,"3","Usama Ali","Kal se office Timing bjy hia","10:00 Am"));
        chat_modelArrayList.add(new Chat_Model(R.drawable.ic_doctor,"5","Hamzai","mujhy bhi yahi time suite krta hai","10:30 Am"));
        chat_modelArrayList.add(new Chat_Model(R.drawable.woman," ","Bilawal","Kal se office Timing bjy hia","10:00 Am"));
        chat_modelArrayList.add(new Chat_Model(R.drawable.ic_doctor,"","Ahmad","Kal se office Timing bjy hia","1 day ago"));
        chat_modelArrayList.add(new Chat_Model(R.drawable.woman,"1","Usama Ali","Kal se office Timing bjy hia","1 day ago"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new RecyclerChatArea(chat_modelArrayList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }
}
