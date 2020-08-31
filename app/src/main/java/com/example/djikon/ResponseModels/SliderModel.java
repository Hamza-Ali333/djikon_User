package com.example.djikon.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class SliderModel {

    private String image;


    public SliderModel(String image) {
        this.image = image;

    }

    public String getImage() {
        return image;
    }

}
