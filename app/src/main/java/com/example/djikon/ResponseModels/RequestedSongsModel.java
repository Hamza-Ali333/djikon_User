package com.example.djikon.ResponseModels;

public class RequestedSongsModel {
    private int id;
    private String firstname,
    lastname,
    created_Date,
    song_name;

    //Date is remaing
    //Song Name also remaing

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCreated_Date() {
        return created_Date;
    }

    public String getSong_name() {
        return song_name;
    }
}
