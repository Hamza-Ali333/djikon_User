package com.example.djikon.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ResponseModels.SingleServiceReviews;
import com.example.djikon.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerServiceReviews extends RecyclerView.Adapter<RecyclerServiceReviews.ViewHolder>{

    private List<SingleServiceReviews> serviceReviewsModels;
    private static final String IMAGE_BASE_URL ="http://ec2-52-91-44-156.compute-1.amazonaws.com/api/";

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{


        public TextView txt_User_Name;
        public RatingBar ratingBar;
        public TextView txt_Review;//msg or comment
        public TextView txt_Created_Date;
        public CircularImageView img_Reviewer_Profile;


        public ViewHolder(View itemView){
            super(itemView);

            img_Reviewer_Profile = itemView.findViewById(R.id.img_commentor);
            txt_User_Name = itemView.findViewById(R.id.txt_user_name);
            ratingBar = itemView.findViewById(R.id.ratbar);
            txt_Review = itemView.findViewById(R.id.txt_body);
            txt_Created_Date = itemView.findViewById(R.id.txt_created_date);
        }
    }

    //constructor
    public RecyclerServiceReviews(List<SingleServiceReviews> serviceReviewsModels) {
        this.serviceReviewsModels = serviceReviewsModels;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SingleServiceReviews currentItem = serviceReviewsModels.get(position);

        if (!currentItem.getImage().equals("") && currentItem.getImage() != null){
            Picasso.get().load(IMAGE_BASE_URL+currentItem.getImage())
                    .placeholder(R.drawable.ic_avatar)
                    .into(holder.img_Reviewer_Profile, new Callback() {
                        @Override
                        public void onSuccess() {

                            // holder.txt_Loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Toast.makeText(getC, "Something Happend Wrong feed image", Toast.LENGTH_LONG).show();
                        }
                    });

        }

        holder.txt_User_Name.setText(currentItem.getName());
        holder.ratingBar.setRating(currentItem.getRating());
        holder.txt_Review.setText(currentItem.getReview());
        holder.txt_Created_Date.setText(currentItem.getCreated_at());


    }

    @Override
    public int getItemCount() {
        return serviceReviewsModels.size();
    }
}
