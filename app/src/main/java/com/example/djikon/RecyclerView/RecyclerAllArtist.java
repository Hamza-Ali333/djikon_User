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
import com.example.djikon.Models.AllArtistModel;
import com.example.djikon.R;

import java.util.List;

public class RecyclerAllArtist extends RecyclerView.Adapter<RecyclerAllArtist.ViewHolder>{

    private List<AllArtistModel> mAllArtistModel;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView txt_Subscribe_Artist_Location;
        public TextView txt_Follow;
        public RelativeLayout rlt_SubscribeArtist;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Subscribe_Artist_Location = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            txt_Follow = itemView.findViewById(R.id.txt_UnFollow);
            rlt_SubscribeArtist = itemView.findViewById(R.id.subscribe_artist_layout);
        }
    }

//constructor
    public RecyclerAllArtist(List<AllArtistModel> allArtistModelList) {
        this.mAllArtistModel = allArtistModelList;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AllArtistModel currentItem = mAllArtistModel.get(position);

      // holder.img_Subscribe_Artist_Profile.setImageResource(currentItem.getImg_Subscribe_Artist());
       holder.txt_Subscribe_Artist_Name.setText(currentItem.getFirstname()+" "+ currentItem.getLastname());
       holder.txt_Subscribe_Artist_Location.setText(currentItem.getLocation());

       holder.txt_Follow.setText("Follow");

       holder.rlt_SubscribeArtist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(view.getContext(), DjProfileActivity.class);
               intent.putExtra("id", currentItem.getId());
               view.getContext().startActivity(intent);
           }
       });

        holder.txt_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.txt_Follow.setText("Unfollow");
                Toast.makeText(view.getContext(), "This Featured will Available Soon", Toast.LENGTH_SHORT).show();
            }
        });

}

    @Override
    public int getItemCount() {
        return mAllArtistModel.size();
    }
}
