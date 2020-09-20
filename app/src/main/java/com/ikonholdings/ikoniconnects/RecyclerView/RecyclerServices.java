package com.ikonholdings.ikoniconnects.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.BookArtistOrServiceActivity;
import com.ikonholdings.ikoniconnects.ResponseModels.ServicesModel;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.ServiceDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerServices extends RecyclerView.Adapter<RecyclerServices.ViewHolder> {

    private List<ServicesModel> mServices;
    private int artistId;
    private Context context;


    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_ServiceName, txt_ServiceDetail, txt_Price,txt_ChargesType;
        public RatingBar ratingBar;
        public ImageView img_featured;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

            img_featured = itemView.findViewById(R.id.img_servic_image);
            txt_ServiceName = itemView.findViewById(R.id.txt_servic_name);
            txt_ServiceDetail = itemView.findViewById(R.id.txt_servic_description);
            txt_Price = itemView.findViewById(R.id.txt_service_prize);
            txt_ChargesType = itemView.findViewById(R.id.pricetype);

            ratingBar = itemView.findViewById(R.id.ratbar);
        }
    }

    //constructor
    public RecyclerServices(List<ServicesModel> services, int artistId, Context context) {
        this.mServices = services;
        this.artistId = artistId;
        this.context = context;
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

                holder.ratingBar.setRating(currentItem.getRating());

        if (!currentItem.getFeature_image().equals("no")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getFeature_image())
                    .fit()
                    .centerCrop()
                    .into(holder.img_featured, new com.squareup.picasso.Callback() {
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
                //sending Featured Image only

                Intent i = new Intent(v.getContext(), ServiceDetailActivity.class);
                i.putExtra("serviceId",currentItem.getId());
                i.putExtra("artistId",artistId);
                v.getContext().startActivity(i);
            }
        });
            }

    @Override
    public int getItemCount() {
        return mServices.size();
    }

    private void addExtraToIntentAndLunchActivity (ImageView img,
                                                   int artistId,
                                                   Boolean bookingForArtist,
                                                   Boolean bookingForService,
                                                   int serviceId,
                                                   String priceType,
                                                   String price,
                                                   String serviceNameOrSubscriberName,
                                                   String description) {

        img.buildDrawingCache();
        Bitmap bitmap = img.getDrawingCache();
        Intent i = new Intent(context, BookArtistOrServiceActivity.class);
        i.putExtra("artistId",artistId);
        i.putExtra("bookingForArtist", bookingForArtist);//booking Artist true
        i.putExtra("bookingForService", bookingForService);//booking Service true
        i.putExtra("serviceId",serviceId);
        i.putExtra("priceType", priceType);
        i.putExtra("BitmapImage", bitmap);
        i.putExtra("price", price);//rate per hour
        i.putExtra("serviceOrSubscriberName", serviceNameOrSubscriberName);//Name of Artist if booking him or Name of Service if Booking Artist Service
        i.putExtra("description", description);
        context.startActivity(i);
    }
}


