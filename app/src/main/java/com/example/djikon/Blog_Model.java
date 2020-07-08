package com.example.djikon;

public class Blog_Model {

    private String title;
    private String description,
            photo,
            likes,
            created_at,
            artist_image,
            artist_profile_link,
            like_link,
            single_blog_link;

    public Blog_Model(String title, String description, String photo, String likes, String created_at, String artist_image, String artist_profile_link, String like_link, String single_blog_link) {
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.likes = likes;
        this.created_at = created_at;
        this.artist_image = artist_image;
        this.artist_profile_link = artist_profile_link;
        this.like_link = like_link;
        this.single_blog_link = single_blog_link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getLikes() {
        return likes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getArtist_image() {
        return artist_image;
    }

    public String getArtist_profile_link() {
        return artist_profile_link;
    }

    public String getLike_link() {
        return like_link;
    }

    public String getSingle_blog_link() {
        return single_blog_link;
    }
}
