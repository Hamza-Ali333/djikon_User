package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerLiveToArtist extends RecyclerView.Adapter<RecyclerLiveToArtist.ViewHolder>{

    private ArrayList<LiveToArtist> mliveToArtistArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_LiveArtistProfile;
        public TextView  txt_LiveArtistName;

        public ViewHolder(View itemView){
            super(itemView);
            img_LiveArtistProfile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_LiveArtistName = itemView.findViewById(R.id.txt_msg_sender_name);
        }
    }

//constructor
    public RecyclerLiveToArtist(ArrayList<LiveToArtist> latestFeedItemArrayList) {
        this.mliveToArtistArrayList = latestFeedItemArrayList;
    }





    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.livetoartisht_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LiveToArtist currentItem = mliveToArtistArrayList.get(position);

        holder.img_LiveArtistProfile.setImageResource(currentItem.getImg_live_artist());
        holder.txt_LiveArtistName.setText(currentItem.getTxt_LiveArtistName());

}

    @Override
    public int getItemCount() {
        return mliveToArtistArrayList.size();
    }
}
