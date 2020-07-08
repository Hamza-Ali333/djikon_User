package com.example.djikon;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(List<SliderItem> sliderItems,Context context) {
        this.context = context;
        this.mSliderItems = sliderItems;

    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem currentItem = mSliderItems.get(position);

               //This is the code for showing title on the image
       // viewHolder.textViewDescription.setText(currentItem.getDescription());
       // viewHolder.textViewDescription.setTextSize(16);
       // viewHolder.textViewDescription.setTextColor(Color.WHITE);

//        Glide.with(viewHolder.itemView)
//                .load(currentItem.getImage())
//                .fitCenter()
//                .into(viewHolder.imageViewBackground);
       // viewHolder.itemView.setBackgroundResource(currentItem.getImage());
        viewHolder.imageViewBackground.setImageResource(currentItem.getImage());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
       // ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slide);
          //imageGifContainer = itemView.findViewById(R.id.slide);
            textViewDescription = itemView.findViewById(R.id.description);
            this.itemView = itemView;
        }
    }

}
