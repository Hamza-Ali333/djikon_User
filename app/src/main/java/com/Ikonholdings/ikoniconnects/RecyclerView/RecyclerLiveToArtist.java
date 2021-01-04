package com.Ikonholdings.ikoniconnects.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.Activity.DjProfileActivity;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.ResponseModels.BookingHistory;
import com.Ikonholdings.ikoniconnects.ResponseModels.CurrentLiveArtistModel;
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
            Picasso.get().load(ApiClient.Base_Url + currentItem.getProfile_image())
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DjProfileActivity.class);
                intent.putExtra("id", currentItem.getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    public void filterList(List<CurrentLiveArtistModel>  list) {
        currentLiveArtistModels = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return currentLiveArtistModels.size();
    }
}
