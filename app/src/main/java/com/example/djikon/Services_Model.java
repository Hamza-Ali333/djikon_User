package com.example.djikon;

public class Services_Model {
    private String name,
    details,
    feature_image,
    price_type,
    rating;

    private int price;

    public Services_Model(String name, String details, String feature_image, int price, String price_type, String rating) {
        this.name = name;
        this.details = details;
        this.feature_image = feature_image;
        this.price = price;
        this.price_type = price_type;
        this.rating = rating;
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

    public String getRating() {
        return rating;
    }
}
