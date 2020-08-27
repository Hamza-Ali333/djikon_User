package com.example.djikon.ResponseModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleServiceModel {

    private Integer id;
    private String name;
    private String description;
    private String price_type;
    private Integer price;
    private String featureImage;
    private String gallery;
    private String artist_name;
    private float rating;

    @SerializedName("reviews")
    @Expose
    private List<SingleServiceReviews> singleServiceReviews = null;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice_type() {
        return price_type;
    }

    public Integer getPrice() {
        return price;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public String getGallery() {
        return gallery;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public float getRating() {
        return rating;
    }

    public List<SingleServiceReviews> getSingleServiceReviews() {
        return singleServiceReviews;
    }

}