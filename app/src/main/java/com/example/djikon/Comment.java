package com.example.djikon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("created_at_date")
    @Expose
    private String createdAtDate;
    @SerializedName("created_at_time")
    @Expose
    private String createdAtTime;



    public String user_name;
    public String user_image;


    public Comment() {
    }

    public Comment(String body, String createdAtDate, String createdAtTime,String user_name,String user_image) {
        super();
        this.body = body;
        this.createdAtDate = createdAtDate;
        this.createdAtTime = createdAtTime;
        this.user_name = user_name;
        this.user_image = user_image;
    }


    public String getBody() {
        return body;
    }

    public String getCreatedAtDate() {
        return createdAtDate;
    }

    public String getCreatedAtTime() {
        return createdAtTime;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_image() {
        return user_image;
    }
}