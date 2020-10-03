package com.ikonholdings.ikoniconnects.OnlineOfflineChat;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangeUserStatus extends AsyncTask<Void,Void,Void> {

    private String status;
    private String userId;

    public ChangeUserStatus(Boolean online, String userId) {
        this.userId = userId;
        if(online) status = "online";
        else status = "offline";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Query filteredData = FirebaseDatabase.getInstance().getReference("Chats")
                .child("chatListOfSubscriber")
                .orderByChild("user_Id")
                .equalTo(userId);

        filteredData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap hashMap = new HashMap();
                hashMap.put("status",status);
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    dataSnapshot.getRef().child("status").updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return null;

    }
}
