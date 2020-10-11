package com.ikonholdings.ikoniconnects.Chat;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.R;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment  {
    private TextView Msg;
    private RecyclerView mRecyclerView;
    private RecyclerGroupChatList mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout pullToRefresh;

    private SearchView mSearchView;

    private AlertDialog loadingDialog;

    private DatabaseReference myRef;
    private List<GroupChatListModel> mGroupChatList;//Contain only those group in which this current User is also added

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_group,container,false);
        createReferences(v);

        myRef = FirebaseDatabase.getInstance().getReference().child("Chats");

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this.getContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);//will make layout reverse
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);//always at new entry at the top
        mRecyclerView.setLayoutManager(mLayoutManager);
        mGroupChatList = new ArrayList<>();

        new GetAllGroupsOfThisSubscriber().execute();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAllGroupsOfThisSubscriber().execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        return v;
    }

    private void filter(String searchText){
        List<GroupChatListModel> filteredlist = new ArrayList<>();
        String ConcatinatName;
        for(GroupChatListModel item: mGroupChatList) {
            ConcatinatName = item.getGroup_Name();
            if(ConcatinatName.toLowerCase().contains(searchText.toLowerCase())){
                filteredlist.add(item);
            }
        }

        mAdapter.filterList(filteredlist);
    }

    private void createReferences(View v) {
        Msg = v.findViewById(R.id.msg);
        mSearchView = v.findViewById(R.id.edt_search);
        mRecyclerView = v.findViewById(R.id.recyclerViewChatGroup);
        pullToRefresh =v.findViewById(R.id.pullToRefresh);
    }

    private class GetAllGroupsOfThisSubscriber extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (! PreferenceData.getUserId(getContext()).isEmpty() && ! PreferenceData.getUserId(getContext()).equals("No Id")) {

                myRef.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            mGroupChatList.clear();
                            List<GroupChatListModel> AllGroups = new ArrayList<>();//Contain All Groups
                            AllGroups.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                GroupChatListModel singleNode = snapshot.getValue(GroupChatListModel.class);
                                AllGroups.add(singleNode);
                            }
//                            for (int i = 0; i < AllGroups.size() ; i++) {
//                                GroupChatListModel list = AllGroups.get(i);
//                                    for (int j = 0; j < list.getGroup_User_Ids().size(); j++) {
//                                        if(list.getGroup_User_Ids().get(j) ==
//                                                Integer.parseInt(PreferenceData.getUserId(getContext()))
//                                        ){
//                                            mGroupChatList.add(list);//Contain only those group in which this current User is also added
//                                        }
//                                    }
//                            }

                            mAdapter = new RecyclerGroupChatList(mGroupChatList, PreferenceData.getUserId(getContext()));
                            mRecyclerView.setAdapter(mAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogsUtils.showAlertDialog(getContext(),
                                false,
                                "Error",
                                "It's seems like you didn't have conversation with any Subscriber");
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mGroupChatList == null){
                Msg.setVisibility(View.VISIBLE);
            }else {
                Msg.setVisibility(View.GONE);
            }
        }
    }

}