package com.example.djikon;

public class Feed_Blog_Model {

        private Integer id;
        private Integer artist_profile_id;
        private String title;
        private String description;
        private String photo;
        private Integer likes;
        private String created_at;
        private String like_link;
        private String artist_image;
        private Integer comments;
        private Integer like_status;

        private String single_blog_link;

        public Feed_Blog_Model(Integer id, Integer artist_profile_id, String title, String description, String photo, Integer likes, String created_at, String likeLink,
                               String artistImage, String singleBlogLink, Integer comments, Integer like_status) {
            super();
            this.id = id;
            this.artist_profile_id = artist_profile_id;
            this.title = title;
            this.description = description;
            this.photo = photo;
            this.likes = likes;
            this.created_at = created_at;
            this.like_link = likeLink;
            this.artist_image = artistImage;
            this.single_blog_link = singleBlogLink;
            this.comments = comments;
            this.like_status = like_status;
        }

    public Integer getLike_status() {
        return like_status;
    }

    public Integer getId() {
            return id;
        }

        public Integer getArtist_profile_id() {
            return artist_profile_id;
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

        public Integer getLikes() {
            return likes;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getLike_link() {
            return like_link;
        }

        public String getArtist_image() {
            return artist_image;
        }

        public String getSingle_blog_link() {
            return single_blog_link;
        }

        public Integer getComments() {
            return comments;
        }

    public void setLike_status(Integer like_status) {
        this.like_status = like_status;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}


