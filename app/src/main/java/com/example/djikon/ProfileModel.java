package com.example.djikon;


import java.util.List;

public class ProfileModel {


    private Integer id;

    private String profile_image,
            firstname,
            lastname,
            email,
            contact,
            about,
            gender,
            location,
            rate_per_hour,
            online_status;


    private Integer followers,
            follow_status;


    private List<Dj_Blogs_Model> blog;

    private List<Services_Model> services;

    public String getOnline_status() {
        return online_status;
    }

    public String getAbout() {
        return about;
    }

    public String getRate_per_hour() {
        return rate_per_hour;
    }

    public Integer getFollowers() {
        return followers;
    }

    public Integer getFollow_status() {
        return follow_status;
    }

    public Integer getId() {
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

    public String getEmail() {
        return email;
    }

    public List<Dj_Blogs_Model> getBlog() {
        return blog;
    }

    public List<Services_Model> getServices() {
        return services;
    }

    public String getContact() {
        return contact;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }
}
