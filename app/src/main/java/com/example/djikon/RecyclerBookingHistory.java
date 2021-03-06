package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerBookingHistory extends RecyclerView.Adapter<RecyclerBookingHistory.ViewHolder>{

    private ArrayList<BookingHistory> mbookingHistoryArrayList;

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
    public RecyclerBookingHistory(ArrayList<BookingHistory> bookingHistoryArrayList) {
        this.mbookingHistoryArrayList = bookingHistoryArrayList;
    }




    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       BookingHistory currentItem = mbookingHistoryArrayList.get(position);

        holder.img_booker_image.setImageResource(currentItem.getImg_booker_image());

        holder.txt_Name.setText(currentItem.getTxt_booker_Name());
        holder.txt_Date.setText(currentItem.getTxt_booked_date());
        holder.txt_EventTitle.setText(currentItem.getTxt_Booked_Event_Tilte());
        holder.txt_Description.setText(currentItem.getTxt_booked_event_discription());
        holder.txt_Charges.setText(currentItem.getTxt_booking_charges());

}

    @Override
    public int getItemCount() {
        return mbookingHistoryArrayList.size();
    }
}
