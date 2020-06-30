package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class RecyclerChatArea extends RecyclerView.Adapter<RecyclerChatArea.ViewHolder>{

    private ArrayList<Chat_Model> mchat_Aera;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_msg_Sender_Profile;
        public TextView txt_msg_Sender_Name;
        public TextView  txt_Last_msg;
        public TextView txt_Recive_Time, txt_UnRead;
        public RelativeLayout rlt_ChatItem;

        public ViewHolder(View itemView){
            super(itemView);
            img_msg_Sender_Profile = itemView.findViewById(R.id.img_msg_sender);

            txt_msg_Sender_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Last_msg = itemView.findViewById(R.id.txt_last_send_msg);
            txt_Recive_Time = itemView.findViewById(R.id.txt_recieve_time);
            txt_UnRead = itemView.findViewById(R.id.txt_unRead_msgs);

            rlt_ChatItem = itemView.findViewById(R.id.rlt_chatitem);

        }
    }

//constructor
    public RecyclerChatArea(ArrayList<Chat_Model> chat_modelArrayList) {
        this.mchat_Aera = chat_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Chat_Model currentItem = mchat_Aera.get(position);

       holder.img_msg_Sender_Profile.setImageResource(currentItem.getImg_msg_sender());
       holder.txt_msg_Sender_Name.setText(currentItem.getMsg_Sender_Name());
       holder.txt_Last_msg.setText(currentItem.getMsg_last_send());
       holder.txt_UnRead.setText(currentItem.getMsg_UnRead());
       holder.txt_Recive_Time.setText(currentItem.getMsg_Recieved_Time());

       holder.rlt_ChatItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(view.getContext(), DjPrpfileActivity.class);
               view.getContext().startActivity(intent);
           }
       });






}

    @Override
    public int getItemCount() {
        return mchat_Aera.size();
    }
}
