package com.ikonholdings.ikoniconnects.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ResponseModels.BookingHistory;
import com.ikonholdings.ikoniconnects.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerBookingHistory extends RecyclerView.Adapter<RecyclerBookingHistory.ViewHolder>{

    private List<BookingHistory> mbookingHistoryArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_more_option;
        public CircularImageView img_booker_image;
        public TextView  txt_Name, txt_Date, txt_EventTitle, txt_Charges;
        private ProgressBar profileProgressBar;

        public ViewHolder(View itemView){
            super(itemView);
            img_booker_image = itemView.findViewById(R.id.img_booker_image);

            txt_Name = itemView.findViewById(R.id.txt_booker_name);
            txt_Date = itemView.findViewById(R.id.txt_booking_date);
            txt_EventTitle = itemView.findViewById(R.id.txt_booked_event_title);
            txt_Charges = itemView.findViewById(R.id.txt_booking_charges);

            img_more_option = itemView.findViewById(R.id.img_booking_option);

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
            Picasso.get().load("http://ec2-52-91-44-156.compute-1.amazonaws.com/" + currentItem.getProfile_image())
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
        holder.txt_EventTitle.setText(currentItem.getName());
        holder.txt_Date.setText(currentItem.getStart_date()+" to "+currentItem.getEnd_date());
        holder.txt_Charges.setText(currentItem.getPrice());

}

    @Override
    public int getItemCount() {
        return mbookingHistoryArrayList.size();
    }
}
