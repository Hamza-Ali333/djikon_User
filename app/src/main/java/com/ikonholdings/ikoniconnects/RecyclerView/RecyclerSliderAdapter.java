package com.ikonholdings.ikoniconnects.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.ResponseModels.SliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSliderAdapter extends
        SliderViewAdapter<RecyclerSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderModel> mSliderModels = new ArrayList<>();


    public RecyclerSliderAdapter(List<SliderModel> sliderModels, Context context) {
        this.context = context;
        this.mSliderModels = sliderModels;

    }

    public void renewItems(List<SliderModel> sliderModels) {
        this.mSliderModels = sliderModels;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderModels.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderModel sliderModel) {
        this.mSliderModels.add(sliderModel);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderModel currentItem = mSliderModels.get(position);

               //This is the code for showing title on the image
       // viewHolder.textViewDescription.setText(currentItem.getDescription());
       // viewHolder.textViewDescription.setTextSize(16);
       // viewHolder.textViewDescription.setTextColor(Color.WHITE);

//        Glide.with(viewHolder.itemView)
//                .load(currentItem.getImage())
//                .fitCenter()
//                .into(viewHolder.imageViewBackground);
       // viewHolder.itemView.setBackgroundResource(currentItem.getImage());


        if(!currentItem.getImage().isEmpty() && !currentItem.getImage().equals("no") ){
            viewHolder.loading.setVisibility(View.VISIBLE);
            Picasso.get().load(currentItem.getImage())
                    .into(viewHolder.imageViewBackground, new Callback() {
                        @Override
                        public void onSuccess() {

                            viewHolder.loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(context, "Something Happend With Slider Image", Toast.LENGTH_LONG).show();
                        }
                    });

        }





        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderModels.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ProgressBar loading;
       // ImageView imageGifContainer;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slide);
            loading = itemView.findViewById(R.id.loading);
          //imageGifContainer = itemView.findViewById(R.id.slide);

            this.itemView = itemView;
        }
    }

}
