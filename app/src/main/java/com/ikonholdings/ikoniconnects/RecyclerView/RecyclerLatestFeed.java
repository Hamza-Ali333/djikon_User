package com.ikonholdings.ikoniconnects.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.BlogDetailActivity;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.DjProfileActivity;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.ResponseModels.FeedBlogModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;
import com.ikonholdings.ikoniconnects.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class RecyclerLatestFeed extends RecyclerView.Adapter<RecyclerLatestFeed.ViewHolder> {

    private List<FeedBlogModel> mBlogModelArrayList;

    private Context context;
    private AlertDialog alertDialog;

    private int total_likes;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_uploaderProfile, img_feedImage, img_Chat, img_Likes;
        public TextView txt_uploaderName, txt_uploadTime, txt_Description, txt_ReadMore, txt_LikesNo, txt_ChatNo;
        public ProgressBar progressBarProfile, progressBarFeed;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBarFeed = itemView.findViewById(R.id.progressBarFead);
            progressBarProfile = itemView.findViewById(R.id.progressBarProfile);
            img_uploaderProfile = itemView.findViewById(R.id.img_uploaderImage);
            img_feedImage = itemView.findViewById(R.id.img_feedImage);
            img_Likes = itemView.findViewById(R.id.img_likes);
            img_Chat = itemView.findViewById(R.id.img_chat);

            txt_uploaderName = itemView.findViewById(R.id.txt_uploaderName);
            txt_uploadTime = itemView.findViewById(R.id.txt_uploadTime);
            txt_Description = itemView.findViewById(R.id.txt_imgDescription);
            txt_LikesNo = itemView.findViewById(R.id.txt_LikesNo);
            txt_ChatNo = itemView.findViewById(R.id.txt_chatNo);
        }
    }

    //constructor
    public RecyclerLatestFeed(List<FeedBlogModel> blogModelArrayList, Context context) {
        this.mBlogModelArrayList = blogModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_latest_feeds_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedBlogModel currentItem = mBlogModelArrayList.get(position);

        if (!currentItem.getArtist_image().equals("no")) {
            holder.progressBarProfile.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getArtist_image())
                    .placeholder(R.drawable.progressbar)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_uploaderProfile, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBarProfile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBarProfile.setVisibility(View.GONE);
                        }
                    });
        }

            if (!currentItem.getPhoto().equals("no")) {
                holder.progressBarFeed.setVisibility(View.VISIBLE);
                Picasso.get().load(ApiClient.Base_Url +currentItem.getPhoto())
                        .fit()
                        .centerCrop()
                        .into(holder.img_feedImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                holder.progressBarFeed.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                holder.progressBarFeed.setVisibility(View.GONE);
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
                        Intent i = new Intent(v.getContext(), DjProfileActivity.class);
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

                        }else  {

                            currentItem.setLike_status(0);

                            likeAndUnlikeBlog(  holder.img_Likes,currentItem.getId().toString(),0);//do unlike
                            total_likes= currentItem.getLikes()-1;
                            currentItem.setLikes(total_likes);
                            holder.img_Likes.setImageResource(R.drawable.ic_unlike);

                        }
                        YoYo.with(Techniques.Wobble)
                                .duration(700)
                                .repeat(0)
                                .playOn(holder.img_Likes);
                        holder.txt_LikesNo.setText(String.valueOf(currentItem.getLikes()));

                    }
                });


                holder.img_Chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Wobble)
                                .duration(700)
                                .repeat(0)
                                .playOn(holder.img_Chat);
                        Intent i = new Intent(v.getContext(), BlogDetailActivity.class);
                        i.putExtra("url",currentItem.getId());
                        i.putExtra("featured_image",currentItem.getPhoto());
                        v.getContext().startActivity(i);
                    }
                });
            }

    @Override
    public int getItemCount() {
        return mBlogModelArrayList.size();
    }

    private void likeAndUnlikeBlog(ImageView view,String BlogID,Integer Status){
        Retrofit retrofit = ApiClient.retrofit(context);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call <SuccessErrorModel> call = jsonApiHolder.LikeUnlike ( "like_blog/"+BlogID, Status);

        call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    view.setClickable(true);
                    view.setEnabled(true);
                    Log.i(TAG, "onResponse: "+response.code());
                }else {
                    alertDialog = DialogsUtils.showResponseMsg(context,
                            false);
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                alertDialog = DialogsUtils.showResponseMsg(context,
                        true);
            }
        });

    }
}


