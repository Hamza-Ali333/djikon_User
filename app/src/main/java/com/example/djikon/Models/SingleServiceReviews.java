package com.example.djikon.Models;

public class SingleServiceReviews {


    private String image;
    private String name;
    private String review;
    private float rating;
    private String created_at;


    public SingleServiceReviews(String image, String name, String review, float rating, String created_at) {
        super();
        this.image = image;
        this.name = name;
        this.review = review;
        this.rating = rating;
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
