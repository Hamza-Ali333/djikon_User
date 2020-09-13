package com.ikonholdings.ikoniconnects.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ResponseModels.RequestedSongsModel;
import com.ikonholdings.ikoniconnects.R;

import java.util.List;

public class RecyclerRequestedSong extends RecyclerView.Adapter<RecyclerRequestedSong.ViewHolder>{

    private List<RequestedSongsModel> mRequestedSongs_models;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Subscriber_Profile;
        public TextView txt_Subscriber_Name;
        public TextView txt_requested_date;
        public TextView txt_Song_Name;


        public ViewHolder(View itemView){
            super(itemView);
            img_Subscriber_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscriber_Name = itemView.findViewById(R.id.txt_subscriber_name);
           // txt_requested_date = itemView.findViewById(R.id.txt_start_date);
            txt_Song_Name = itemView.findViewById(R.id.txt_song_name);

        }
    }

    //constructor
    public RecyclerRequestedSong(List<RequestedSongsModel> requestedSongsmodels) {
        this.mRequestedSongs_models = requestedSongsmodels;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_requested_song,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RequestedSongsModel currentItem = mRequestedSongs_models.get(position);

        holder.txt_Subscriber_Name.setText(currentItem.getFirstname() +" "+ currentItem.getLastname());
        holder.txt_Song_Name.setText(currentItem.getSong_name());

//        if(currentItem.getCreated_Date() != null){
//            Picasso.get().load(currentItem.getCreated_Date())
//                    .placeholder(R.drawable.ic_avatar)
//                    .fit()
//                    .centerCrop()
//                    .into(holder.img_Subscriber_Profile, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//                        @Override
//                        public void onError(Exception e) {
//
//                        }
//                    });
    //    }//if

}

    @Override
    public int getItemCount() {
        return mRequestedSongs_models.size();
    }
}
