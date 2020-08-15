package com.example.djikon.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.Models.BookingHistory;
import com.example.djikon.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerBookingHistory extends RecyclerView.Adapter<RecyclerBookingHistory.ViewHolder>{

    private List<BookingHistory> mbookingHistoryArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_booker_image, img_more_option;
        public TextView  txt_Name, txt_Date, txt_EventTitle, txt_Description, txt_Charges;

        public ViewHolder(View itemView){
            super(itemView);
            img_booker_image = itemView.findViewById(R.id.img_booker_image);

            txt_Name = itemView.findViewById(R.id.txt_booker_name);
            txt_Date = itemView.findViewById(R.id.txt_booking_date);
            txt_Description = itemView.findViewById(R.id.txt_booking_description);
            txt_EventTitle = itemView.findViewById(R.id.txt_booked_event_title);
            txt_Charges = itemView.findViewById(R.id.txt_booking_charges);


            img_more_option = itemView.findViewById(R.id.img_booking_option);

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

        //holder.img_booker_image.setImageResource(currentItem.getImg_booker_image());

        holder.txt_Name.setText(currentItem.getFirstname()+" "+currentItem.getLastname());
        holder.txt_Date.setText(currentItem.getStart_date()+" to "+currentItem.getEnd_date());
        holder.txt_EventTitle.setText(currentItem.getName());
        holder.txt_Description.setText("No Description getttin From APi");
        holder.txt_Charges.setText(currentItem.getPrice());

}

    @Override
    public int getItemCount() {
        return mbookingHistoryArrayList.size();
    }
}
