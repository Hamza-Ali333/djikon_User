package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerServiceGallery extends RecyclerView.Adapter<RecyclerServiceGallery.ViewHolder>{

    private List<SliderItem> mServiceImage;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_gallery_Image;




        public ViewHolder(View itemView){
            super(itemView);
            img_gallery_Image = itemView.findViewById(R.id.service_image);
        }
    }

    //constructor
    public RecyclerServiceGallery(List<SliderItem> sliderItems) {
        this.mServiceImage = sliderItems;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_gallery_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SliderItem currentItem = mServiceImage.get(position);


        if (!currentItem.getImage().equals("no")){
            Picasso.get().load(currentItem.getImage())
                    .fit()
                    .centerCrop()
                    .into(holder.img_gallery_Image, new Callback() {
                        @Override
                        public void onSuccess() {

                            // holder.txt_Loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Toast.makeText(getC, "Something Happend Wrong feed image", Toast.LENGTH_LONG).show();
                        }
                    });

        }

}

    @Override
    public int getItemCount() {
        return mServiceImage.size();
    }
}
