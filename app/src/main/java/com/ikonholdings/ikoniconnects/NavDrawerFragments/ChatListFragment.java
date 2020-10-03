package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.OnlineOfflineChat.StatusModel;
import com.ikonholdings.ikoniconnects.ResponseModels.UserChatListModel;
import com.ikonholdings.ikoniconnects.Notification.Token;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerChatList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


public class ChatListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txt_progressMsg;
    private SwipeRefreshLayout pullToRefresh;

    private DatabaseReference myRef;
    private List<UserChatListModel> mUserChatList;

    private String currentUserId;

    private FirebaseUser fuser;

    private AlertDialog loadingDialog;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_area,container,false);
        createReferences(v);

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);//will make layout reverse
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);//always at new entry at the top
        mRecyclerView.setLayoutManager(mLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        //this will contain the currentUser id
        currentUserId = PreferenceData.getUserId(getContext());

        myRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        mUserChatList = new ArrayList<>();
        getChatList();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatList();
                pullToRefresh.setRefreshing(false);
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return v;
    }


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        if(fuser != null)
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void getChatList(){
        loadingDialog = DialogsUtils.showLoadingDialogue(getContext());

        //if get some data or not get the default value(No Email) form preferences
        if (!currentUserId.isEmpty() && !currentUserId.equals("No Id")) {
            myRef.child("chatListOfUser").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        mUserChatList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            mUserChatList.add(new UserChatListModel(
                                    snapshot.child("subscriber_Id").getValue(String.class),
                                    snapshot.child("subscriber_Uid").getValue(String.class),
                                    snapshot.child("subscriber_Name").getValue(String.class),
                                    snapshot.child("imgProfileUrl").getValue(String.class),
                                    snapshot.child("status").getValue(String.class),
                                    snapshot.getKey()
                            ));
                        }

                        mAdapter = new RecyclerChatList(mUserChatList, currentUserId);
                        mRecyclerView.setAdapter(mAdapter);
                        loadingDialog.dismiss();

                    }else {
                        loadingDialog.dismiss();
                        DialogsUtils.showAlertDialog(getContext(),false,"Note","It's seems like you didn't have conversation with any Subscriber");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    loadingDialog.dismiss();
                    DialogsUtils.showAlertDialog(getContext(),false,"Error","It's seems like you didn't have conversation with any Subscriber");
                }
            });
        }
    }

    private void createReferences(View v) {

        mRecyclerView = v.findViewById(R.id.recyclerView_Chat);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
    }



}
