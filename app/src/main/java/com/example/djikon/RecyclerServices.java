package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerServices extends RecyclerView.Adapter<RecyclerServices.ViewHolder> {

    private List<Services_Model> mServices;


    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_ServiceName, txt_ServiceDetail, txt_Price,txt_ChargesType;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_ServiceName = itemView.findViewById(R.id.txt_servic_name);
            txt_ServiceDetail = itemView.findViewById(R.id.txt_servic_description);
            txt_Price = itemView.findViewById(R.id.txt_service_prize);
            txt_ChargesType = itemView.findViewById(R.id.pricetype);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), String.valueOf(itemView.getId()), Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(view.getContext(), ServiceDetailActivity.class);
//                    view.getContext().startActivity(i);
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
                holder.txt_Price.setText(String.valueOf(currentItem.getPrice()));




            }



    @Override
    public int getItemCount() {
        return mServices.size();
    }
}


