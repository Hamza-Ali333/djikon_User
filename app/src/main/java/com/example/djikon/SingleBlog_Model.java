package com.example.djikon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleBlog_Model {

    //Galler is also an array oh no

    private String title,
    created_at,
    description,
    video,
    artist_name,
    artist_profile_image,
    gallery;



    private int  id, likes_count,
            comments_count;

    @SerializedName("comments")
    @Expose
    public List<Comment> comments = null;


    public SingleBlog_Model(String title, String created_at, String gallery ,
                            String description, String video, String artist_name, String artist_profile_image,
                            int likes_count, int comments_count , int id, List<Comment> comments) {
        this.title = title;
        this.created_at = created_at;
        this.description = description;
        this.gallery = gallery;
        this.video = video;
        this.artist_name = artist_name;
        this.artist_profile_image = artist_profile_image;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.id = id;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getGallery() {
        return gallery;
    }

    public String getDescription() {
        return description;
    }

    public String getVideo() {
        return video;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getArtist_profile_image() {
        return artist_profile_image;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }
}
