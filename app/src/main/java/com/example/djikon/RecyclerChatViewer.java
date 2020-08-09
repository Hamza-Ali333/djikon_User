package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerChatViewer extends RecyclerView.Adapter<RecyclerChatViewer.ViewHolder>{

    private List<ChatModel> mChat_model;
    public  String currentUserEmail;

    public static final int MSG_TYPE_RIGHT = 0;
    public static final int MSG_TYPE_LEFT = 1;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Profile;
        public TextView txt_msg;

        public TextView txt_Recive_Time;
        public RelativeLayout rlt_ChatItem;

        public ViewHolder(View itemView){
            super(itemView);
            img_Profile = itemView.findViewById(R.id.profile_image);


            txt_msg = itemView.findViewById(R.id.msg);


            //rlt_ChatItem = itemView.findViewById(R.id.rlt_chatitem);
        }
    }

//constructor
    public RecyclerChatViewer(List<ChatModel> chat_modelList,String currentUserEmail) {
        this.mChat_model = chat_modelList;
        this.currentUserEmail = currentUserEmail;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v;
        if (viewType == MSG_TYPE_RIGHT) {

             v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);

        }else {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final ChatModel currentItem = mChat_model.get(position);

       holder.txt_msg.setText(currentItem.getMessage());

       //image setting remaining

/*       holder.rlt_ChatItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

//               Intent i = new Intent(view.getContext(),ChatViewerActivity.class);
//               i.putExtra("email",currentItem.getMsg_Sender_Name());
//               view.getContext().startActivity(i);

           }
       });*/

}

    @Override
    public int getItemViewType(int position) {

        if(mChat_model.get(position).getSender().equals(currentUserEmail)){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChat_model.size();
    }
}
