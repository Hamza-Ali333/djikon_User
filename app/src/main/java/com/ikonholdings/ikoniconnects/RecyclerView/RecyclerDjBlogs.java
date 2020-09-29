package com.ikonholdings.ikoniconnects.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.BlogDetailActivity;
import com.ikonholdings.ikoniconnects.ResponseModels.DjProfileBlogsModel;
import com.ikonholdings.ikoniconnects.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerDjBlogs extends RecyclerView.Adapter<RecyclerDjBlogs.ViewHolder> {

    private List<DjProfileBlogsModel> subscriberBlogsModels;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_BlogTitle;
        public ImageView img_blog;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_blog = itemView.findViewById(R.id.blog_image);
            txt_BlogTitle = itemView.findViewById(R.id.blog_title);
            progressBar = itemView.findViewById(R.id.progressBarBlog);
        }
    }

    //constructor
    public RecyclerDjBlogs(List<DjProfileBlogsModel> blogsModels) {
        this.subscriberBlogsModels = blogsModels;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dj_blog, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DjProfileBlogsModel currentItem = subscriberBlogsModels.get(position);

        holder.txt_BlogTitle.setText(currentItem.getTitle());

        if (currentItem.getPhoto()!= null && !currentItem.getPhoto().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url +currentItem.getPhoto())
                    .fit()
                    .centerCrop()
                    .into(holder.img_blog, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BlogDetailActivity.class);
                i.putExtra("url",currentItem.getId());
                v.getContext().startActivity(i);
            }
        });

            }



    @Override
    public int getItemCount() {
        return subscriberBlogsModels.size();
    }
}


