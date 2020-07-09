package com.example.djikon;

import android.content.Intent;

public class SliderItem {
    private String image;
    private String description;

    public SliderItem(String image, String description) {
        this.image = image;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
