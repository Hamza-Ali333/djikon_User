package com.Ikonholdings.ikoniconnects.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.Activity.DjProfileActivity;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.GlobelClasses.FollowUnFollowArtist;
import com.Ikonholdings.ikoniconnects.GlobelClasses.ProgressButton;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.ResponseModels.SubscribeArtistModel;
import com.Ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerSubscribedArtist extends RecyclerView.Adapter<RecyclerSubscribedArtist.ViewHolder> implements Filterable {

    private List<SubscribeArtistModel> mSubscribeArtistList;
    private List<SubscribeArtistModel> mSearchList;

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
    public RecyclerSubscribedArtist(List<SubscribeArtistModel> subscribeArtistList) {
        this.mSubscribeArtistList = subscribeArtistList;
        mSearchList = new ArrayList<>(subscribeArtistList);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SubscribeArtistModel currentItem = mSubscribeArtistList.get(position);
        holder.unFollow = holder.progressButton.findViewById(R.id.btn_title);
        holder.unFollow.setText("UnFollow");

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
//                view.setEnabled(false);
//                view.setClickable(false);
//                new UnFollowSubscriber(currentItem.getId(),view).execute();
            }
        });
}

    @Override
    public int getItemCount() {
        return mSubscribeArtistList.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SubscribeArtistModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0 ) {
                filteredList.addAll(mSearchList);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                String nameConcatination;

                for(SubscribeArtistModel item : mSearchList){
                    nameConcatination= item.getFirstname()+" "+item.getLastname();
                    if(nameConcatination.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mSubscribeArtistList.clear();
            mSubscribeArtistList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    private class UnFollowSubscriber extends AsyncTask<Void,Void,Void> {

        Integer artistID;
        Context context;
        private ProgressButton mProgressButton;
        public UnFollowSubscriber(Integer artistID, View view) {
            this.artistID = artistID;
            this.context = view.getContext();
            mProgressButton = new ProgressButton(context,view);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressButton.btnOnClick("UnFollowing...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = ApiClient.retrofit(context);
            JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

            String relativeUrl = "";
            //0 means not following yet

            relativeUrl = "unfollow_artist/"+artistID;


            Call<SuccessErrorModel> call = jsonApiHolder.followUnFollowArtist(relativeUrl);

            call.enqueue(new retrofit2.Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                    if(response.isSuccessful()){
                        for (int i = 0; i < mSubscribeArtistList.size() ; i++) {
                            if(artistID.equals(mSubscribeArtistList.get(i).getId())){
                                mSubscribeArtistList.remove(i);
                                notifyItemRangeRemoved(i,mSubscribeArtistList.size());
                                Log.i("TAG", "onResponse: "+mSubscribeArtistList);
                            }
                        }
                        mProgressButton.btnOnCompelet("UnFollowed");
                    }else if(response.code() == 400){
                        DialogsUtils.showAlertDialog(context,
                                false,
                                "Note",
                                "You can't unfollow this Subscriber.\n" +
                                        "Your account is created by this Subscriber referral");

                        mProgressButton.btnOnCompelet("UnFollow");

                    }
                    //response is not successful
                  else {
                        mProgressButton.btnOnCompelet("UnFollow");
                        DialogsUtils.showResponseMsg(context,false);
                        }
                    }//response is not successful
                //onResponse
                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    mProgressButton.btnOnCompelet("UnFollow");
                    DialogsUtils.showResponseMsg(context,true);
                }
            });
            return null;
        }
    }

}
