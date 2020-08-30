package com.example.djikon.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ResponseModels.RequestedSongsModel;
import com.example.djikon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerRequestedSong extends RecyclerView.Adapter<RecyclerRequestedSong.ViewHolder>{

    private List<RequestedSongsModel> mRequestedSongs_models;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_DJ_Profile;
        public TextView txt_DJ_Name;
        public TextView txt_requested_date;
        public TextView txt_Cancle;


        public ViewHolder(View itemView){
            super(itemView);
            img_DJ_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_DJ_Name = itemView.findViewById(R.id.txt_requester_name);
            txt_requested_date = itemView.findViewById(R.id.txt_start_date);


        }
    }

    //constructor
    public RecyclerRequestedSong(List<RequestedSongsModel> requestedSongsmodels) {
        this.mRequestedSongs_models = requestedSongsmodels;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_requested_song,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
         RequestedSongsModel currentItem = mRequestedSongs_models.get(position);

        holder.txt_DJ_Name.setText(currentItem.getFirstname()+""+ currentItem.getLastname());
       // holder.txt_requested_date.setText(currentItem.getDate());
        if(currentItem.getProfile_image() != null){
            Picasso.get().load(currentItem.getProfile_image())
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(holder.img_DJ_Profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onError(Exception e) {
                            //Toast.makeText(g, "Something Happend Wrong feed image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }//if

//        holder.txt_Cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mRequestedSongs_models.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mRequestedSongs_models.size());
//            }
//        });
}

    @Override
    public int getItemCount() {
        return mRequestedSongs_models.size();
    }
}
