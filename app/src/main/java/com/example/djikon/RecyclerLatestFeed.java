package com.example.djikon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class RecyclerLatestFeed extends RecyclerView.Adapter<RecyclerLatestFeed.ViewHolder> {

    private List<Feed_Blog_Model> mBlogModelArrayList;

    private Context context;

    private static final String URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/like_blog/";
    private int total_likes;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_uploaderProfile, img_feedImage, img_Chat, img_Likes;
        public TextView txt_uploaderName, txt_uploadTime, txt_Description, txt_ReadMore, txt_LikesNo, txt_ChatNo, txt_Loading;



        public ViewHolder(View itemView) {
            super(itemView);
            img_uploaderProfile = itemView.findViewById(R.id.img_uploaderImage);
            img_feedImage = itemView.findViewById(R.id.img_feedImage);
            img_Likes = itemView.findViewById(R.id.img_likes);
            img_Chat = itemView.findViewById(R.id.img_chat);

            txt_uploaderName = itemView.findViewById(R.id.txt_uploaderName);
            txt_uploadTime = itemView.findViewById(R.id.txt_uploadTime);
            txt_Description = itemView.findViewById(R.id.txt_imgDescription);
            txt_LikesNo = itemView.findViewById(R.id.txt_LikesNo);
            txt_ChatNo = itemView.findViewById(R.id.txt_chatNo);
            txt_Loading = itemView.findViewById(R.id.loading);
        }
    }

    //constructor
    public RecyclerLatestFeed(List<Feed_Blog_Model> blogModelArrayList, Context context) {
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
        Feed_Blog_Model currentItem = mBlogModelArrayList.get(position);

        if (!currentItem.getArtist_image().equals("no")) {

            Picasso.get().load(currentItem.getArtist_image())
                    .fit()
                    .placeholder(R.drawable.ic_doctor)
                    .into(holder.img_uploaderProfile, new Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(context, "Something Happend Wrong Uploader Image", Toast.LENGTH_LONG).show();
                        }
                    });
        }


            if (!currentItem.getPhoto().equals("no")) {
                holder.txt_Loading.setVisibility(View.VISIBLE);
                Picasso.get().load(currentItem.getPhoto())
                        .fit()
                        .into(holder.img_feedImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                holder.txt_Loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(context, "Something Happend Wrong feed image", Toast.LENGTH_LONG).show();
                            }
                        });
            }


                holder.txt_uploaderName.setText(currentItem.getTitle());
                holder.txt_uploadTime.setText(currentItem.getCreated_at());
                holder.txt_Description.setText(currentItem.getDescription());
                total_likes= currentItem.getLikes();
                holder.txt_LikesNo.setText(String.valueOf(total_likes));
                holder.txt_ChatNo.setText(String.valueOf(currentItem.getComments()));

                holder.img_uploaderProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), DjPrpfileActivity.class);
                        i.putExtra("id", currentItem.getArtist_profile_id());
                        v.getContext().startActivity(i);
                    }
                });

                holder.img_feedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(v.getContext(), BlogDetailActivity.class);
                        i.putExtra("url",currentItem.getId());
                        i.putExtra("featured_image",currentItem.getPhoto());
                        v.getContext().startActivity(i);
                    }
                });


                //0 means unlik
                if(currentItem.getLike_status()==0){
                    holder.img_Likes.setImageResource(R.drawable.ic_unlike);
                }else {
                    holder.img_Likes.setImageResource(R.drawable.ic_heart_fill);
                }


                holder.img_Likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.img_Likes.setEnabled(false);
                        holder.img_Likes.setClickable(false);

                        //0 means unlik
                        if(currentItem.getLike_status() == 0){
                            currentItem.setLike_status(1);
                            total_likes = currentItem.getLikes()+1;
                            currentItem.setLikes(total_likes);
                            likeAndUnlikeBlog( holder.img_Likes,currentItem.getId().toString(),1);//do like and make status=1
                            holder.img_Likes.setImageResource(R.drawable.ic_heart_fill);

                            holder.txt_LikesNo.setText(String.valueOf(currentItem.getLikes()));
                        }else  {

                            currentItem.setLike_status(0);

                            likeAndUnlikeBlog(  holder.img_Likes,currentItem.getId().toString(),0);//do unlike
                            total_likes= currentItem.getLikes()-1;
                            currentItem.setLikes(total_likes);
                            holder.img_Likes.setImageResource(R.drawable.ic_unlike);
                            holder.txt_LikesNo.setText(String.valueOf(currentItem.getLikes()));

                        }

                    }
                });


            }

    @Override
    public int getItemCount() {
        return mBlogModelArrayList.size();
    }

    private void likeAndUnlikeBlog(ImageView view,String BlogID,Integer Status){
        Retrofit retrofit = ApiResponse.retrofit(URL,context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call <SuccessErrorModel> call = jsonApiHolder.LikeUnlike ( BlogID, Status);

        call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    view.setClickable(true);
                    view.setEnabled(true);
                    Log.i(TAG, "onResponse: "+response.code());
                }else {
                    Log.i(TAG, "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                Log.i(TAG, "onResponse: "+t.getMessage());
            }
        });

    }
}


