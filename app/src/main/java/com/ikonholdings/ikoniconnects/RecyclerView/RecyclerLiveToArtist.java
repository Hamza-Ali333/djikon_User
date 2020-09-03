package com.ikonholdings.ikoniconnects.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.ResponseModels.CurrentLiveArtistModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerLiveToArtist extends RecyclerView.Adapter<RecyclerLiveToArtist.ViewHolder> {

    private List<CurrentLiveArtistModel> currentLiveArtistModels;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView img_LiveArtistProfile;
        public TextView txt_LiveArtistName;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_LiveArtistProfile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_LiveArtistName = itemView.findViewById(R.id.txt_msg_sender_name);
            progressBar = itemView.findViewById(R.id.progressBarProfile);
        }
    }

    //constructor
    public RecyclerLiveToArtist(List<CurrentLiveArtistModel> liveArtistModels) {
        this.currentLiveArtistModels = liveArtistModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_live_artisht, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CurrentLiveArtistModel currentItem = currentLiveArtistModels.get(position);

        holder.txt_LiveArtistName.setText(currentItem.getFirstname() + " " + currentItem.getLastname());
        if (currentItem.getProfile_image() != null && !currentItem.getProfile_image().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load("http://ec2-52-91-44-156.compute-1.amazonaws.com/" + currentItem.getProfile_image())
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(holder.img_LiveArtistProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }//if


    }

    @Override
    public int getItemCount() {
        return currentLiveArtistModels.size();
    }
}
