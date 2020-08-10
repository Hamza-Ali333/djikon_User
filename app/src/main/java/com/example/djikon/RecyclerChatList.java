package com.example.djikon;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerChatList extends RecyclerView.Adapter<RecyclerChatList.ViewHolder>{

    private List<UserChatListModel> mChat_Aera;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_DJ_Profile;
        public TextView txt_msg_Sender_Name;
        public TextView  txt_Last_msg;
        public TextView txt_Recive_Time, txt_UnRead;
        public RelativeLayout rlt_ChatItem;

        public ViewHolder(View itemView){
            super(itemView);
            img_msg_DJ_Profile = itemView.findViewById(R.id.img_msg_sender);

            txt_msg_Sender_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Last_msg = itemView.findViewById(R.id.txt_last_send_msg);
            txt_Recive_Time = itemView.findViewById(R.id.txt_recieve_time);
            txt_UnRead = itemView.findViewById(R.id.txt_unRead_msgs);

            rlt_ChatItem = itemView.findViewById(R.id.rlt_chatitem);

        }


    }

//constructor
    public RecyclerChatList(List<UserChatListModel> chat_List_modelArrayList) {
        this.mChat_Aera = chat_List_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final UserChatListModel currentItem = mChat_Aera.get(position);


       holder.txt_msg_Sender_Name.setText(currentItem.getDj_Name());

        if (!currentItem.getImageUrl().equals("no")) {

            Picasso.get().load(currentItem.getImageUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_doctor)
                    .into(holder.img_msg_DJ_Profile, new Callback() {
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
       holder.txt_UnRead.setText(currentItem.getId());
//       holder.txt_Recive_Time.setText(currentItem.getMsg_Recieved_Time());



       holder.rlt_ChatItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(view.getContext(),ChatViewerActivity.class);
               i.putExtra("id",Integer.parseInt(currentItem.getId()));
               i.putExtra("djName",currentItem.getDj_Name());
               i.putExtra("imgProfileUrl",currentItem.getImageUrl());
               view.getContext().startActivity(i);
               Log.i("Recycler", "onClick: id "+currentItem.getDj_Name());
           }
       });

       holder.rlt_ChatItem.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               final CharSequence[] items = {"Delete Chat"};

               AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

               builder.setTitle("Select The Action");
               builder.setItems(items, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int item) {
                   }
               });
               builder.show();
               return true;
           }
       });

}

    @Override
    public int getItemCount() {
        return mChat_Aera.size();
    }
}
