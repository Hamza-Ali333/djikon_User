package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerServiceGallery extends RecyclerView.Adapter<RecyclerServiceGallery.ViewHolder>{

    private ArrayList<ServiceImage_Model> mServiceImage;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_gallery_Image;
        public TextView txt_Image_Name;



        public ViewHolder(View itemView){
            super(itemView);
            img_gallery_Image = itemView.findViewById(R.id.service_image);

            txt_Image_Name = itemView.findViewById(R.id.service_image_name);




        }
    }

//constructor
    public RecyclerServiceGallery(ArrayList<ServiceImage_Model> serviceImage_modelArrayList) {
        this.mServiceImage = serviceImage_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_gallery_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ServiceImage_Model currentItem = mServiceImage.get(position);

       holder.img_gallery_Image.setImageResource(currentItem.getService_Gallery_Image());
       holder.txt_Image_Name.setText(currentItem.getService_Image_Name());


}

    @Override
    public int getItemCount() {
        return mServiceImage.size();
    }
}
