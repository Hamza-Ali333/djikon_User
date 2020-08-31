package com.example.djikon.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.DjProfileActivity;
import com.example.djikon.GlobelClasses.FollowUnFollowArtist;
import com.example.djikon.GlobelClasses.ProgressButton;
import com.example.djikon.ResponseModels.SubscribeArtistModel;
import com.example.djikon.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerSubscribedArtist extends RecyclerView.Adapter<RecyclerSubscribedArtist.ViewHolder>{

    private List<SubscribeArtistModel> mSubscribeToArtistArrayList;
    private static final String Image_Base_Url ="http://ec2-52-91-44-156.compute-1.amazonaws.com/";

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView txt_Subscribe_Artist_Location;
        public  View progressButton;
        public TextView unFollow;
        public ProgressBar ProgressBarProfile;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Subscribe_Artist_Location = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            progressButton = itemView.findViewById(R.id.progress_button);
            ProgressBarProfile = itemView.findViewById(R.id.progressBarProfile);
        }
    }

//constructor
    public RecyclerSubscribedArtist(List<SubscribeArtistModel> subscribeToArtistArrayList) {
        this.mSubscribeToArtistArrayList = subscribeToArtistArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SubscribeArtistModel currentItem = mSubscribeToArtistArrayList.get(position);
        holder.unFollow = holder.progressButton.findViewById(R.id.btn_title);
        holder.unFollow.setText("UnFollow");

        if (currentItem.getProfile_image() != null && !currentItem.getProfile_image().equals("no")) {
            holder.ProgressBarProfile.setVisibility(View.VISIBLE);
            Picasso.get().load(Image_Base_Url + currentItem.getProfile_image())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_Subscribe_Artist_Profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.ProgressBarProfile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.ProgressBarProfile.setVisibility(View.GONE);
                        }
                    });
        }
       holder.txt_Subscribe_Artist_Name.setText(currentItem.getFirstname()+" "+ currentItem.getLastname());
       holder.txt_Subscribe_Artist_Location.setText(currentItem.getLocation());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(view.getContext(), DjProfileActivity.class);
               intent.putExtra("id", currentItem.getId());
               view.getContext().startActivity(intent);
           }
       });

        holder.progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUnFollowArtist(1,
                        String.valueOf(currentItem.getId()),
                        view.getContext(),
                        view).execute();
            }
        });


}

    @Override
    public int getItemCount() {
        return mSubscribeToArtistArrayList.size();
    }
}
