package com.example.djikon.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicesModel {
    private int id;
    private String name,
    details,
    feature_image,
    price_type;
    float rating;

    private int price;


    public ServicesModel(int id, String name, String details, String feature_image, String price_type, float rating, int price) {

        this.id = id;
        this.name = name;
        this.details = details;
        this.feature_image = feature_image;
        this.price_type = price_type;
        this.rating = rating;
        this.price = price;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public int getPrice() {
        return price;
    }

    public String getPrice_type() {
        return price_type;
    }

    public float getRating() {
        return rating;
    }
}
