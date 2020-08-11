package com.example.djikon.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.Models.ServicesModel;
import com.example.djikon.R;
import com.example.djikon.ServiceDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerServices extends RecyclerView.Adapter<RecyclerServices.ViewHolder> {

    private List<ServicesModel> mServices;


    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_ServiceName, txt_ServiceDetail, txt_Price,txt_ChargesType;
        public ImageView img_featured;

        public ViewHolder(View itemView) {
            super(itemView);
            img_featured = itemView.findViewById(R.id.img_servic_image);
            txt_ServiceName = itemView.findViewById(R.id.txt_servic_name);
            txt_ServiceDetail = itemView.findViewById(R.id.txt_servic_description);
            txt_Price = itemView.findViewById(R.id.txt_service_prize);
            txt_ChargesType = itemView.findViewById(R.id.pricetype);


        }
    }

    //constructor
    public RecyclerServices(List<ServicesModel> services) {
        this.mServices = services;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_services, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServicesModel currentItem = mServices.get(position);

                holder.txt_ServiceName.setText(currentItem.getName());
                holder.txt_ServiceDetail.setText(currentItem.getDetails());
                holder.txt_ChargesType.setText(currentItem.getPrice_type());
                holder.txt_Price.setText("$"+String.valueOf(currentItem.getPrice())+" ");

        if (!currentItem.getFeature_image().equals("no")) {
            Picasso.get().load(currentItem.getFeature_image())
                    .fit()
                    .centerCrop()
                    .into(holder.img_featured, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {


                        }
                        @Override
                        public void onError(Exception e) {
                          // Toast.makeText(g, "Something Happend Wrong feed image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        holder.img_featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sending Featured Image only
                holder.img_featured.buildDrawingCache();
                Bitmap bitmap = holder.img_featured.getDrawingCache();

                Intent i = new Intent(v.getContext(), ServiceDetailActivity.class);
                i.putExtra("id",currentItem.getId());
                i.putExtra("BitmapImage", bitmap);
                v.getContext().startActivity(i);
            }
        });
            }



    @Override
    public int getItemCount() {
        return mServices.size();
    }
}


