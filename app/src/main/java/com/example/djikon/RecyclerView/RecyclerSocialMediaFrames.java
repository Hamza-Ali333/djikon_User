package com.example.djikon.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.NavDrawerFragments.SocialMediaShareFragment;
import com.example.djikon.R;
import com.example.djikon.ResponseModels.FramesModel;
import com.example.djikon.ResponseModels.SliderModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerSocialMediaFrames extends RecyclerView.Adapter<RecyclerSocialMediaFrames.ViewHolder> {

    private List<FramesModel> frameImageList;
    private Context context;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView img_LiveArtistProfile;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_LiveArtistProfile = itemView.findViewById(R.id.frame);
            progressBar = itemView.findViewById(R.id.progressBarProfile);

        }
    }

    //constructor
    public RecyclerSocialMediaFrames(List<FramesModel> liveArtistModels, Context context) {
        this.frameImageList = liveArtistModels;
        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frams, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FramesModel currentItem = frameImageList.get(position);
        Log.i("TAG", "onBindViewHolder: "+currentItem);

        if (currentItem.getFrame() != null && !currentItem.getFrame().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getFrame())
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
        return frameImageList.size();
    }
}
