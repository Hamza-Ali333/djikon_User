package com.example.djikon.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.BlogDetailActivity;
import com.example.djikon.ResponseModels.DjProfileBlogsModel;
import com.example.djikon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerDjBlogs extends RecyclerView.Adapter<RecyclerDjBlogs.ViewHolder> {

    private List<DjProfileBlogsModel> djBlogsModels;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_BlogTitle;
        public ImageView img_blog;
        public ProgressBar progressBar;

        public static final String IMAGERELATIVELAYOUT="http://ec2-52-91-44-156.compute-1.amazonaws.com/";

        public ViewHolder(View itemView) {
            super(itemView);
            img_blog = itemView.findViewById(R.id.blog_image);
            txt_BlogTitle = itemView.findViewById(R.id.blog_title);
            progressBar = itemView.findViewById(R.id.progressBarBlog);
        }
    }

    //constructor
    public RecyclerDjBlogs(List<DjProfileBlogsModel> blogsModels) {
        this.djBlogsModels = blogsModels;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dj_blog, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DjProfileBlogsModel currentItem = djBlogsModels.get(position);

        holder.txt_BlogTitle.setText(currentItem.getTitle());

        if (!currentItem.getPhoto().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(ViewHolder.IMAGERELATIVELAYOUT+currentItem.getPhoto())
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
        return djBlogsModels.size();
    }
}


