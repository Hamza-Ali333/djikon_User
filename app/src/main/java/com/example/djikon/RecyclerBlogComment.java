package com.example.djikon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerBlogComment extends RecyclerView.Adapter<RecyclerBlogComment.ViewHolder>{

    private List<Comment> mchat_Aera;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{


        public TextView txt_User_Name;
        public TextView txt_Body;//msg or comment
        public TextView txt_Created_Date;


        public ViewHolder(View itemView){
            super(itemView);


            txt_User_Name = itemView.findViewById(R.id.txt_user_name);
            txt_Body = itemView.findViewById(R.id.txt_body);
            txt_Created_Date = itemView.findViewById(R.id.txt_created_date);




        }
    }

//constructor
    public RecyclerBlogComment(List<Comment> chat_modelArrayList) {
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
        final Comment currentItem = mchat_Aera.get(position);


      // holder.txt_User_Name.setText(currentItem);
       holder.txt_Body.setText(currentItem.getBody());
       holder.txt_Created_Date.setText(currentItem.getCreatedAtDate());


}

    @Override
    public int getItemCount() {
        return mchat_Aera.size();
    }
}
