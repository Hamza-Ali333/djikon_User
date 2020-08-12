package com.example.djikon.Models;

public class AllArtistModel {
    private int id;
    private String profile_image, firstname, lastname, location, online_status, follow_status;

    public AllArtistModel(int id, String profile_image, String firstname, String lastname, String location, String online_status, String follow_status) {
        this.id = id;
        this.profile_image = profile_image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.online_status = online_status;
        this.follow_status = follow_status;
    }

    public int getId() {
        return id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLocation() {
        return location;
    }

    public String getOnline_status() {
        return online_status;
    }

    public String getFollow_status() {
        return follow_status;
    }
}
