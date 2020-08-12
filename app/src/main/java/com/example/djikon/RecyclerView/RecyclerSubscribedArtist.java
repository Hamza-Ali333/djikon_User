package com.example.djikon.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.DjProfileActivity;
import com.example.djikon.Models.SubscribeArtistModel;
import com.example.djikon.R;

import java.util.List;

public class RecyclerSubscribedArtist extends RecyclerView.Adapter<RecyclerSubscribedArtist.ViewHolder>{

    private List<SubscribeArtistModel> mSubscribeToArtistArrayList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView txt_Subscribe_Artist_Location;
        public TextView  txt_UnFollow;
        public RelativeLayout rlt_SubscribeArtist;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Subscribe_Artist_Location = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            txt_UnFollow = itemView.findViewById(R.id.txt_UnFollow);
            rlt_SubscribeArtist = itemView.findViewById(R.id.subscribe_artist_layout);
        }
    }

//constructor
    public RecyclerSubscribedArtist(List<SubscribeArtistModel> subscribeToArtistArrayList) {
        this.mSubscribeToArtistArrayList = subscribeToArtistArrayList;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscribe_artist,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SubscribeArtistModel currentItem = mSubscribeToArtistArrayList.get(position);

      // holder.img_Subscribe_Artist_Profile.setImageResource(currentItem.getImg_Subscribe_Artist());
       holder.txt_Subscribe_Artist_Name.setText(currentItem.getFirstname()+" "+ currentItem.getLastname());
       holder.txt_Subscribe_Artist_Location.setText(currentItem.getLocation());

       holder.rlt_SubscribeArtist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(view.getContext(), DjProfileActivity.class);
               intent.putExtra("id", currentItem.getId());
               view.getContext().startActivity(intent);
           }
       });

        holder.txt_UnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "This Featured will Available Soon", Toast.LENGTH_SHORT).show();
            }
        });




}

    @Override
    public int getItemCount() {
        return mSubscribeToArtistArrayList.size();
    }
}
