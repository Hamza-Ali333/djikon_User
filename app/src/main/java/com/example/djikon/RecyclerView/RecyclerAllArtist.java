package com.example.djikon.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.BlogDetailActivity;
import com.example.djikon.DjProfileActivity;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.FollowUnFollowArtist;
import com.example.djikon.ResponseModels.AllArtistModel;
import com.example.djikon.ResponseModels.SuccessErrorModel;
import com.example.djikon.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerAllArtist extends RecyclerView.Adapter<RecyclerAllArtist.ViewHolder>{

    private List<AllArtistModel> mAllArtistModel;
    private Context context;
    private static final String Image_Base_Url ="http://ec2-52-91-44-156.compute-1.amazonaws.com/";

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView txt_Subscribe_Artist_Location;
        public TextView unFollow;
        public View progressButton;
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
    public RecyclerAllArtist(List<AllArtistModel> allArtistModelList,Context context) {
        this.mAllArtistModel = allArtistModelList;
        this.context = context;
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
        holder.unFollow = holder.progressButton.findViewById(R.id.btn_title);
        holder.unFollow.setText("Follow");

        if (!currentItem.getProfile_image().equals("no")) {
            holder.ProgressBarProfile.setVisibility(View.VISIBLE);
            Picasso.get().load(Image_Base_Url + currentItem.getProfile_image())
                    .placeholder(R.drawable.progressbar)
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

           holder.progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUnFollowArtist(0,
                        String.valueOf(currentItem.getId()),
                        context,
                        view).execute();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DjProfileActivity.class);
                intent.putExtra("id", currentItem.getId());
                v.getContext().startActivity(intent);
            }
        });
}

    @Override
    public int getItemCount() {
        return mAllArtistModel.size();
    }

}
