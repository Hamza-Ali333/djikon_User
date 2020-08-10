package com.example.djikon;

import android.app.AlertDialog;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    private ProgressBar mProgressBar;
    private TextView txt_progressMsg;
    private SwipeRefreshLayout pullToRefresh;

    DatabaseReference myRef;
    List<UserChatListModel> mUserChatList;

    String currentUserId;

    AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_area,container,false);
        createReferences(v);

        showHideProgressBar();//show progressbar while downloading

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);//will make layout reverse
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);//always at new entry at the top
        mRecyclerView.setLayoutManager(mLayoutManager);

        //this will contain the currentUser id
        currentUserId = PreferenceData.getUserId(getContext());

        myRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        mUserChatList = new ArrayList<>();
        getChatList();


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showHideProgressBar();
                getChatList();
                pullToRefresh.setRefreshing(false);
            }
        });



        return v;
    }


    private void getChatList(){
        //if get some data or not get the default value(No Email) form preferences
        if (!currentUserId.isEmpty() && !currentUserId.equals("No Id")) {

            myRef.child("chatListOfUser").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                        mUserChatList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            mUserChatList.add(new UserChatListModel(
                                    snapshot.child("id").getValue(String.class),
                                    snapshot.child("dj_Name").getValue(String.class),
                                    snapshot.child("imageUrl").getValue(String.class)
                            ));
                        }

                        mAdapter = new RecyclerChatList(mUserChatList);
                        mRecyclerView.setAdapter(mAdapter);
                        showHideProgressBar();

                    }else {
                        mAlertDialog = DialogsUtils.showAlertDialog(getContext(),false,"Note","It's seems like you didn't have conversation with any DJ");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mAlertDialog = DialogsUtils.showAlertDialog(getContext(),false,"Error","It's seems like you didn't have conversation with any DJ");
                }
            });
        }
    }

    private void createReferences(View v) {
        mProgressBar = v.findViewById(R.id.progress_circular);
        txt_progressMsg = v.findViewById(R.id.progress_msg);
        mRecyclerView = v.findViewById(R.id.recyclerView_Chat);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);

    }

    private void showHideProgressBar(){
        if (mProgressBar.getVisibility()== View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
            txt_progressMsg.setVisibility(View.GONE);
        }else
        {
            mProgressBar.setVisibility(View.VISIBLE);
            txt_progressMsg.setVisibility(View.VISIBLE);
        }

    }



}
