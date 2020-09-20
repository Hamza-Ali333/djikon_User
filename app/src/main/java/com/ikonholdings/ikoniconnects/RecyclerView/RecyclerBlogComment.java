package com.ikonholdings.ikoniconnects.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ResponseModels.CommentModel;
import com.ikonholdings.ikoniconnects.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerBlogComment extends RecyclerView.Adapter<RecyclerBlogComment.ViewHolder>{

    private List<CommentModel> mchat_Aera;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{


        public TextView txt_User_Name;
        public TextView txt_Body;//msg or comment
        public TextView txt_Created_Date;
        public CircularImageView img_commnetor_Profile;


        public ViewHolder(View itemView){
            super(itemView);

            img_commnetor_Profile = itemView.findViewById(R.id.img_commentor);
            txt_User_Name = itemView.findViewById(R.id.txt_user_name);
            txt_Body = itemView.findViewById(R.id.txt_body);
            txt_Created_Date = itemView.findViewById(R.id.txt_created_date);
        }
    }

//constructor
    public RecyclerBlogComment(List<CommentModel> chat_modelArrayList) {
        this.mchat_Aera = chat_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CommentModel currentItem = mchat_Aera.get(position);

        if (!currentItem.getUser_image().equals("no")){
            Picasso.get().load(currentItem.getUser_image())
                    .into(holder.img_commnetor_Profile, new Callback() {
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

       holder.txt_User_Name.setText(currentItem.user_name);
       holder.txt_Body.setText(currentItem.getBody());
       holder.txt_Created_Date.setText(currentItem.getCreatedAtDate());


}

    @Override
    public int getItemCount() {
        return mchat_Aera.size();
    }
}
