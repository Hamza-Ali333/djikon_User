package com.example.djikon.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.ResponseModels.AllArtistModel;
import com.example.djikon.ResponseModels.SuccessErrorModel;
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
    private Boolean doingWorking;
    private int itemIdWorkingFor;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView img_Subscribe_Artist_Profile;
        public TextView txt_Subscribe_Artist_Name;
        public TextView txt_Subscribe_Artist_Location;
        public TextView txt_Follow;

        public ViewHolder(View itemView){
            super(itemView);
            img_Subscribe_Artist_Profile = itemView.findViewById(R.id.img_SubscribeArtist);

            txt_Subscribe_Artist_Name = itemView.findViewById(R.id.txt_msg_sender_name);
            txt_Subscribe_Artist_Location = itemView.findViewById(R.id.txt_SubscribeArtistStatus);
            txt_Follow = itemView.findViewById(R.id.txt_UnFollow);

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

      // holder.img_Subscribe_Artist_Profile.setImageResource(currentItem.getImg_Subscribe_Artist());
       holder.txt_Subscribe_Artist_Name.setText(currentItem.getFirstname()+" "+ currentItem.getLastname());
       holder.txt_Subscribe_Artist_Location.setText(currentItem.getLocation());

       holder.txt_Follow.setText("Follow");

           holder.txt_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.txt_Follow.setText("Followed");
                new FollowArtist(currentItem.getFollow_status(),
                        String.valueOf(currentItem.getId())).execute();

                int newPosition = holder.getAdapterPosition();
                mAllArtistModel.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, mAllArtistModel.size());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
}

    @Override
    public int getItemCount() {
        return mAllArtistModel.size();
    }


    private class FollowArtist extends AsyncTask<Void,Void,Void> {
        int CurrentStatus;
        String artistID;


        public FollowArtist(int CurrentStatus, String artistID) {
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

                    }else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog = DialogsUtils.showAlertDialog(context,
                                        false,
                                        "Error",
                                        "Something happened wrong try again");
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog = DialogsUtils.showAlertDialog(context,
                                    false,
                                    "Server Not Connected",
                                    "Please Check Your Internet Connection and Try Again!");
                        }
                    });

                }
            });
            return null;
        }

    }
}
