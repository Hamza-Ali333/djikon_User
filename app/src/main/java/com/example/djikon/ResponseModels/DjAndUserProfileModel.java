package com.example.djikon.ResponseModels;


import java.util.List;

public class DjAndUserProfileModel {


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

    private List<DjProfileBlogsModel> blog;

    private List<ServicesModel> services;

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

    public List<DjProfileBlogsModel> getBlog() {
        return blog;
    }

    public List<ServicesModel> getServices() {
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
