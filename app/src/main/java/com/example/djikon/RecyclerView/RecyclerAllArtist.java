package com.example.djikon.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.DjProfileActivity;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.Models.AllArtistModel;
import com.example.djikon.Models.SuccessErrorModel;
import com.example.djikon.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerAllArtist extends RecyclerView.Adapter<RecyclerAllArtist.ViewHolder>{

    private List<AllArtistModel> mAllArtistModel;
    private AlertDialog alertDialog;
    private Context context;

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

    private void followUnFollow (int CurrentStatus, String artistID, Context context) {

    }

    @Override
    public int getItemCount() {
        return mAllArtistModel.size();
    }


    private class FollowUnFollowArtist extends AsyncTask<Void,Void,Void> {
        int CurrentStatus;
        String artistID;

        public FollowUnFollowArtist(int CurrentStatus,String artistID) {
            this.CurrentStatus = CurrentStatus;
            this.artistID = artistID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

            String relativeUrl = "";
            //0 means not following yet
            if(CurrentStatus == 0){
                relativeUrl = "api/follow_artist/"+artistID;
            }else {
                relativeUrl = "api/unfollow_artist/"+artistID;
            }

            Call<SuccessErrorModel> call = jsonApiHolder.followUnFollowArtist(relativeUrl);

            call.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        if (CurrentStatus == 0){
//                        snackBarText.setText(" Follow Successfully");
//                        mFollow_Status = 1;
//                        mFollower_Count++;
//                        btn_Follow.setText("UnFollow");
                        }
                        else {
//                        mFollow_Status = 0;
//                        mFollower_Count--;
//                        snackBarText.setText(" UnFollow Successfully");
//                        btn_Follow.setText("Follow");
                        }

//                    snackbar.show();
//                    btn_Follow.setClickable(false);
//                    btn_Follow.setEnabled(false);
                    }else {
                        alertDialog = DialogsUtils.showAlertDialog(context,
                                false,
                                "Error",
                                "Something happened wrong try again");
                    }
                }
                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                    alertDialog = DialogsUtils.showAlertDialog(context,false,"No Internet","Please Check Your Internet Connection");
                }
            });
            return null;
        }
    }
}
