package com.ikonholdings.ikoniconnects.Chat.Recycler;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.Chat.Activity.GroupChatViewerActivity;
import com.ikonholdings.ikoniconnects.Chat.Models.GroupChatListModel;
import com.ikonholdings.ikoniconnects.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class RecyclerGroupChatList extends RecyclerView.Adapter<RecyclerGroupChatList.ViewHolder>{

    private List<GroupChatListModel> mChatList;
    private DatabaseReference myRef;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Subscriber_Profile;
        public TextView txt_msg_Sender_Name;
       // public TextView  txt_Last_msg,txt_Recive_Time;
        public TextView  txt_UnRead;

        public ViewHolder(View itemView){
            super(itemView);
            img_msg_Subscriber_Profile = itemView.findViewById(R.id.img_msg_sender);

            txt_msg_Sender_Name = itemView.findViewById(R.id.txt_msg_sender_name);

        }

    }

//constructor
    public RecyclerGroupChatList(List<GroupChatListModel> chat_List_modelArrayList, String currentUserId) {
        this.mChatList = chat_List_modelArrayList;
        myRef = FirebaseDatabase.getInstance().getReference("Chats").child("chatListOfUser").child(currentUserId);
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final GroupChatListModel currentItem = mChatList.get(position);

       holder.txt_msg_Sender_Name.setText(currentItem.getGroup_Name());

        if (currentItem.getGroup_Profile() != null && !currentItem.getGroup_Profile().equals("no")) {

            Picasso.get().load(ApiClient.Base_Url+currentItem.getGroup_Profile())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_msg_Subscriber_Profile);
        }


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(view.getContext(), GroupChatViewerActivity.class);
               i.putExtra("list",(Serializable)currentItem.getGroup_User_Ids());
               i.putExtra("groupKey",currentItem.getGroupId());
               i.putExtra("creatorId",currentItem.getCreator_Id());
               i.putExtra("groupName",currentItem.getGroup_Name());
               i.putExtra("groupImage",currentItem.getGroup_Profile());
               view.getContext().startActivity(i);

           }
       });

        //long clicked
       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public boolean onLongClick(View view) {
               PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.itemView);
               popupMenu.inflate(R.menu.chat_option);
               popupMenu.setGravity(Gravity.END);
               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.delete:
                              deleteNode(currentItem.getGroupId(),position);
                               break;
                           default:
                               break;
                       }
                       return true;
                   }
               });
               popupMenu.show();
               return true;
           }
       });

}

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public void filterList(List<GroupChatListModel> list) {
        mChatList = list;
        notifyDataSetChanged();
    }

    private void deleteNode(String Key,int position) {
        mChatList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChatList.size());

        myRef.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                Log.i("TAG", "onDataChange: Done ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("TAG", "onDataChange: Cancle ");
            }
        });
    }


}