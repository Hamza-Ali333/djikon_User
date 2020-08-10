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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference myRef;
    List<UserChatListModel> mUserChatList;
    List<String>usersList;
    String currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_area,container,false);

        mRecyclerView = v.findViewById(R.id.recyclerView_Chat);
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //this will contain the currentUser Email Address
        currentUser = "Hamza";

        myRef = FirebaseDatabase.getInstance().getReference().child("chats");

        usersList = new ArrayList<>();
        //if get some data or not get the default value(No Email) form preferences
        if (!currentUser.isEmpty() && !currentUser.equals("No Email")) {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersList.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        Query queryRef = myRef.orderByChild("sender").equalTo("Hamza");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        ArrayList<UserChatListModel> chat_List_modelArrayList = new ArrayList<>();

        chat_List_modelArrayList.add(new UserChatListModel(R.drawable.woman,"3","Usama Ali","Kal se office Timing bjy hia","10:00 Am"));
        chat_List_modelArrayList.add(new UserChatListModel(R.drawable.ic_doctor,"5","Hamzai","mujhy bhi yahi time suite krta hai","10:30 Am"));
        chat_List_modelArrayList.add(new UserChatListModel(R.drawable.woman," ","Bilawal","Kal se office Timing bjy hia","10:00 Am"));
        chat_List_modelArrayList.add(new UserChatListModel(R.drawable.ic_doctor,"","Ahmad","Kal se office Timing bjy hia","1 day ago"));
        chat_List_modelArrayList.add(new UserChatListModel(R.drawable.woman,"1","Usama Ali","Kal se office Timing bjy hia","1 day ago"));


        mAdapter = new RecyclerChatList(chat_List_modelArrayList);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }
}
