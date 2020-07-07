package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerLatestFeed extends RecyclerView.Adapter<RecyclerLatestFeed.ViewHolder>{

    private ArrayList<LatestFeedItem> mLatestFeedItemArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_uploaderProfile, img_feedImage, img_Chat, img_Likes;
        public TextView txt_uploaderName, txt_uploadTime, txt_Description, txt_ReadMore, txt_LikesNo, txt_ChatNo;

        public ViewHolder(View itemView){
            super(itemView);
            img_uploaderProfile = itemView.findViewById(R.id.img_uploaderImage);
            img_feedImage = itemView.findViewById(R.id.img_feedImage);
            img_Likes = itemView.findViewById(R.id.img_likes);
            img_Chat = itemView.findViewById(R.id.img_chat);

            txt_uploaderName = itemView.findViewById(R.id.txt_uploaderName);
            txt_uploadTime = itemView.findViewById(R.id.txt_uploadTime);
            txt_Description = itemView.findViewById(R.id.txt_imgDescription);
            txt_ReadMore = itemView.findViewById(R.id.txt_ReadMore);
            txt_LikesNo = itemView.findViewById(R.id.txt_LikesNo);
            txt_ChatNo = itemView.findViewById(R.id.txt_chatNo);
        }
    }

//constructor
    public RecyclerLatestFeed (ArrayList<LatestFeedItem> latestFeedItemArrayList) {
        this.mLatestFeedItemArrayList = latestFeedItemArrayList;
    }





    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_feeds_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LatestFeedItem currentItem = mLatestFeedItemArrayList.get(position);

        holder.img_uploaderProfile.setImageResource(currentItem.getImg_UploaderProfile());
        holder.img_feedImage.setImageResource(currentItem.getImg_FeedImage());


        holder.txt_uploaderName.setText(currentItem.getTxt_UploaderName());
        holder.txt_uploadTime.setText(currentItem.getTxt_UploadTime());
        holder.txt_Description.setText(currentItem.getTxt_Description());
        holder.txt_LikesNo.setText(currentItem.getTxt_LikesNo());
        holder.txt_ChatNo.setText(currentItem.getTxt_ChatNo());

        holder.img_uploaderProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DjPrpfileActivity.class);
                v.getContext().startActivity(i);
            }
        });

        holder.img_feedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DjPrpfileActivity.class);
                v.getContext().startActivity(i);
            }
        });

        holder.img_feedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BlogDetailActivity.class);
                v.getContext().startActivity(i);
            }
        });

}

    @Override
    public int getItemCount() {
        return mLatestFeedItemArrayList.size();
    }
}
