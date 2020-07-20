package com.example.djikon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerServices extends RecyclerView.Adapter<RecyclerServices.ViewHolder> {

    private List<Services_Model> mServices;


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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), ServiceDetailActivity.class);
                    view.getContext().startActivity(i);
                }
            });
        }
    }

    //constructor
    public RecyclerServices(List<Services_Model> services) {
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
        Services_Model currentItem = mServices.get(position);

                holder.txt_ServiceName.setText(currentItem.getName());
                holder.txt_ServiceDetail.setText(currentItem.getDetails());
                holder.txt_ChargesType.setText(currentItem.getPrice_type());
                holder.txt_Price.setText("$"+String.valueOf(currentItem.getPrice()));

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
            }



    @Override
    public int getItemCount() {
        return mServices.size();
    }
}


