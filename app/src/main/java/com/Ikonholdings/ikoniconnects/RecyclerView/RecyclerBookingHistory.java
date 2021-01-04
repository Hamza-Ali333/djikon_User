package com.Ikonholdings.ikoniconnects.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects.Activity.DjProfileActivity;
import com.Ikonholdings.ikoniconnects.Activity.ServiceDetailActivity;
import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ResponseModels.BookingHistory;
import com.Ikonholdings.ikoniconnects.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerBookingHistory extends RecyclerView.Adapter<RecyclerBookingHistory.ViewHolder>{

    private List<BookingHistory> mbookingHistoryArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_booker_image;
        public TextView  txt_Name, txt_Date, txt_Status, txt_Charges;
        private ProgressBar profileProgressBar;

        public ViewHolder(View itemView){
            super(itemView);
            img_booker_image = itemView.findViewById(R.id.img_booker_image);

            txt_Name = itemView.findViewById(R.id.txt_booker_name);
            txt_Status = itemView.findViewById(R.id.status);
            txt_Date = itemView.findViewById(R.id.txt_booking_date);
            txt_Charges = itemView.findViewById(R.id.txt_booking_charges);

            profileProgressBar = itemView.findViewById(R.id.progressBarProfile);

        }
    }

//constructor
    public RecyclerBookingHistory(List<BookingHistory> bookingHistoryArrayList) {
        this.mbookingHistoryArrayList = bookingHistoryArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       BookingHistory currentItem = mbookingHistoryArrayList.get(position);

        if (currentItem.getProfile_image() != null && !currentItem.getProfile_image().equals("no")) {
            holder.profileProgressBar.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url + currentItem.getProfile_image())
                    .fit()
                    .placeholder(R.drawable.ic_avatar)
                    .centerCrop()
                    .into(holder.img_booker_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.profileProgressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) {
                            holder.profileProgressBar.setVisibility(View.GONE);
                           // holder.img_booker_image.setImageResource(R.drawable.ic_avatar);
                        }
                    });
        }

        holder.txt_Name.setText(currentItem.getFirstname()+" "+currentItem.getLastname());
        holder.txt_Date.setText(currentItem.getStart_date()+" to "+currentItem.getEnd_date());
        holder.txt_Charges.setText("$"+currentItem.getPrice());

        if(currentItem.getStatus().equals("1")){
            holder.txt_Status.setText("Completed");
        }
        else if (currentItem.getStatus().equals("0")){
            holder.txt_Status.setText("Pending");
            holder.txt_Status.setBackgroundResource(R.drawable.bg_booking_pending);
            holder.txt_Status.setTextColor(ContextCompat.getColor(holder.txt_Status.getContext(),R.color.colorYellow));
        }
        else {
            holder.txt_Status.setText("ReJected");
            holder.txt_Status.setTextColor(ContextCompat.getColor(holder.txt_Status.getContext(),R.color.colorgoogle));
            holder.txt_Status.setBackgroundResource(R.drawable.bg_booking_rejected);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem.getService_id() != null && currentItem.getService_id() != 0){
                    Intent i = new Intent(v.getContext(), ServiceDetailActivity.class);
                    i.putExtra("serviceId",currentItem.getService_id());
                    v.getContext().startActivity(i);
                }else {
                    Intent i = new Intent(v.getContext(), DjProfileActivity.class);
                    i.putExtra("id", currentItem.getSub_id());
                    v.getContext().startActivity(i);
                }

            }
        });

        holder.img_booker_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DjProfileActivity.class);
                i.putExtra("id", currentItem.getSub_id());
                v.getContext().startActivity(i);

            }
        });

}

    public void filterList(List<BookingHistory> list) {
        mbookingHistoryArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mbookingHistoryArrayList.size();
    }
}
