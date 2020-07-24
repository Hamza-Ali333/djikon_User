package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerSubscribeArtist extends RecyclerView.Adapter<RecyclerSubscribeArtist.ViewHolder>{

    private ArrayList<SubscribeToArtist> mSubscribeToArtistArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView  txt_Subscribe_Artist_Status;
        public TextView  txt_UnFollow;
        public RelativeLayout rlt_SubscribeArtist;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Subscribe_Artist_Status = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            txt_UnFollow = itemView.findViewById(R.id.txt_UnFollow);
            rlt_SubscribeArtist = itemView.findViewById(R.id.subscribe_artist_layout);
        }
    }

//constructor
    public RecyclerSubscribeArtist(ArrayList<SubscribeToArtist> subscribeToArtistArrayList) {
        this.mSubscribeToArtistArrayList = subscribeToArtistArrayList;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_artist_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SubscribeToArtist currentItem = mSubscribeToArtistArrayList.get(position);

       holder.img_Subscribe_Artist_Profile.setImageResource(currentItem.getImg_Subscribe_Artist());
       holder.txt_Subscribe_Artist_Name.setText(currentItem.getTxt_SubscribeArtistName());
       holder.txt_Subscribe_Artist_Status.setText(currentItem.getTxt_SubscribeArtistStatus());

       holder.rlt_SubscribeArtist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(view.getContext(), DjPrpfileActivity.class);
               view.getContext().startActivity(intent);
           }
       });

        holder.txt_UnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });




}

    @Override
    public int getItemCount() {
        return mSubscribeToArtistArrayList.size();
    }
}
