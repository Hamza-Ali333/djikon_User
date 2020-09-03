package com.ikonholdings.ikoniconnects.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.ResponseModels.SliderModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerServiceGallery extends RecyclerView.Adapter<RecyclerServiceGallery.ViewHolder>{

    private List<SliderModel> mServiceImage;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_gallery_Image;
        public ProgressBar mProgressBar;


        public ViewHolder(View itemView){
            super(itemView);
            img_gallery_Image = itemView.findViewById(R.id.service_image);
            mProgressBar = itemView.findViewById(R.id.imageProgress);
        }
    }

    //constructor
    public RecyclerServiceGallery(List<SliderModel> sliderModels) {
        this.mServiceImage = sliderModels;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_gallery,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SliderModel currentItem = mServiceImage.get(position);

        if (!currentItem.getImage().equals("no")){
            Picasso.get().load(currentItem.getImage())
                    .fit()
                    .centerCrop()
                    .into(holder.img_gallery_Image, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.mProgressBar.setVisibility(View.GONE);
                        }
                    });

        }

}

    @Override
    public int getItemCount() {
        return mServiceImage.size();
    }
}
