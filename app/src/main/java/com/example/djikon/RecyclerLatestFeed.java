package com.example.djikon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerLatestFeed extends RecyclerView.Adapter<RecyclerLatestFeed.ViewHolder> {

    private List<Blog_Model> mBlogModelArrayList;
    private Context context;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_uploaderProfile, img_feedImage, img_Chat, img_Likes;
        public TextView txt_uploaderName, txt_uploadTime, txt_Description, txt_ReadMore, txt_LikesNo, txt_ChatNo;

        public ViewHolder(View itemView) {
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
    public RecyclerLatestFeed(List<Blog_Model> blogModelArrayList, Context context) {
        this.mBlogModelArrayList = blogModelArrayList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_feeds_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Blog_Model currentItem = mBlogModelArrayList.get(position);

        if (!currentItem.getArtist_image().isEmpty()) {
            Picasso.get().load(currentItem.getArtist_image())
                    .placeholder(R.drawable.ic_doctor)
                    .into(holder.img_uploaderProfile, new Callback() {
                        @Override
                        public void onSuccess() {

                            //holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(context, "Something Happend Wrong Blog Image", Toast.LENGTH_LONG).show();
                        }
                    });
        }


            if (!currentItem.getPhoto().isEmpty()) {
                Picasso.get().load(currentItem.getPhoto())
                        .placeholder(R.drawable.rectangle2)
                        .into(holder.img_feedImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                //holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(context, "Something Happend Wrong Uploader Profile", Toast.LENGTH_LONG).show();
                            }
                        });
            }


                holder.txt_uploaderName.setText(currentItem.getTitle());
                holder.txt_uploadTime.setText(currentItem.getCreated_at());
                holder.txt_Description.setText(currentItem.getDescription());
                holder.txt_LikesNo.setText(currentItem.getLikes());
                // holder.txt_ChatNo.setText(currentItem.getTxt_ChatNo());

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
                       // Toast.makeText(context, currentItem.getSingle_blog_link(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(v.getContext(), BlogDetailActivity.class);
                        i.putExtra("url",currentItem.getId());
                        v.getContext().startActivity(i);
                    }
                });



            }



    @Override
    public int getItemCount() {
        return mBlogModelArrayList.size();
    }
}


