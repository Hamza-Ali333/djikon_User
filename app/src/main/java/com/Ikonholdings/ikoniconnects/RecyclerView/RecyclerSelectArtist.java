package com.Ikonholdings.ikoniconnects.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.ResponseModels.SubscribeArtistModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerSelectArtist extends RecyclerView.Adapter<RecyclerSelectArtist.ViewHolder> {

    private List<SubscribeArtistModel> mSubscribeArtistList;

    private RecyclerSelectArtist.onItemClickListner onItemClickListner;

    private static int LastCheckPosition = -1;

    public interface onItemClickListner{
        void add(Integer id);
        void remove(Integer id);
    }

    //initailizing
    public void setOnItemClickListener(RecyclerSelectArtist.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView txt_Subscribe_Artist_Location;
        public ProgressBar ProgressBarProfile;
        public CheckBox mCheckBox;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Subscribe_Artist_Location = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            ProgressBarProfile = itemView.findViewById(R.id.progressBarProfile);
            mCheckBox = itemView.findViewById(R.id.checkBox);
        }
    }

//constructor
    public RecyclerSelectArtist(List<SubscribeArtistModel> subscribeArtistList) {
        this.mSubscribeArtistList = subscribeArtistList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_artist,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SubscribeArtistModel currentItem = mSubscribeArtistList.get(position);

        if (currentItem.getProfile_image() != null && !currentItem.getProfile_image().equals("no")) {
            holder.ProgressBarProfile.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url + currentItem.getProfile_image())
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
       holder.mCheckBox.setChecked(currentItem.getCheckState());

       holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //if User Click Last Check item again so Just unchecked it and change the value of the variable
               if (position == LastCheckPosition) {
                   //item removed
                   holder.mCheckBox.setChecked(false);
                   LastCheckPosition = -1;
                   onItemClickListner.remove(currentItem.getId());
               } else {
                   //Item checked
                   //Same or Change for both
                   if (LastCheckPosition != -1)
                   mSubscribeArtistList.get(LastCheckPosition).setCheckState(false);

                   mSubscribeArtistList.get(position).setCheckState(true);
                   LastCheckPosition = position;
                   onItemClickListner.add(currentItem.getId());
                   notifyDataSetChanged();
               }

           }
       });
}

    @Override
    public int getItemCount() {
        return mSubscribeArtistList.size();
    }




}
