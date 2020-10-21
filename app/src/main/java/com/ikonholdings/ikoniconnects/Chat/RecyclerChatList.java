package com.ikonholdings.ikoniconnects.Chat;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ResponseModels.UserChatListModel;
import com.ikonholdings.ikoniconnects.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerChatList extends RecyclerView.Adapter<RecyclerChatList.ViewHolder>{

    private List<UserChatListModel> mChat_Aera;
    private DatabaseReference myRef;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Subscriber_Profile;
        public TextView txt_msg_Sender_Name;
       // public TextView  txt_Last_msg,txt_Recive_Time;
        public TextView  txt_UnRead;
        public ImageView offline, online;

        public ViewHolder(View itemView){
            super(itemView);
            img_msg_Subscriber_Profile = itemView.findViewById(R.id.img_msg_sender);

            txt_msg_Sender_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            offline = itemView.findViewById(R.id.offline);
            online = itemView.findViewById(R.id.online);
//            txt_Last_msg = itemView.findViewById(R.id.txt_last_send_msg);
//            txt_Recive_Time = itemView.findViewById(R.id.txt_recieve_time);
//            txt_UnRead = itemView.findViewById(R.id.txt_unRead_msgs);

        }

    }

//constructor
    public RecyclerChatList(List<UserChatListModel> chat_List_modelArrayList, String currentUserId) {
        this.mChat_Aera = chat_List_modelArrayList;
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
       final UserChatListModel currentItem = mChat_Aera.get(position);

       holder.txt_msg_Sender_Name.setText(currentItem.getSubscriber_Name());


//           if(currentItem.getStatus().equals("online")) holder.online.setVisibility(View.VISIBLE);
//           else holder.offline.setVisibility(View.VISIBLE);


        if (!currentItem.getImgProfileUrl().equals("no")) {

            Picasso.get().load(ApiClient.Base_Url+currentItem.getImgProfileUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_msg_Subscriber_Profile, new Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {
                            //Toast.makeText(, "Something Happend Wrong Uploader Image", Toast.LENGTH_LONG).show();
                        }
                    });
        }

//       holder.txt_Last_msg.setText(currentItem.getMsg_last_send());
//       holder.txt_UnRead.setText(currentItem.getId());
//       holder.txt_Recive_Time.setText(currentItem.getMsg_Recieved_Time());


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(view.getContext(), ChatViewerActivity.class);
               i.putExtra("subscriber_Id", currentItem.getSubscriber_Id());
               i.putExtra("subscriber_Name", currentItem.getSubscriber_Name());
               i.putExtra("imgProfileUrl", currentItem.getImgProfileUrl());
               view.getContext().startActivity(i);
           }
       });

        //long clicked
       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public boolean onLongClick(View view) {
              // final CharSequence[] items = {"Delete Chat"};
//               AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//
//               builder.setTitle("Select The Action");
//               builder.setItems(items, new DialogInterface.OnClickListener() {
//                   @Override
//                   public void onClick(DialogInterface dialog, int item) {
//                   }
//               });
//               builder.show();
               PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.itemView);
               popupMenu.inflate(R.menu.chat_option);
               popupMenu.setGravity(Gravity.END);
               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.delete:
                              deleteNode(currentItem.getKey(),position);
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
        return mChat_Aera.size();
    }

    public void filterList(List<UserChatListModel> list) {
        mChat_Aera = list;
        notifyDataSetChanged();
    }

    private void deleteNode(String Key,int position) {
        mChat_Aera.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChat_Aera.size());

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
