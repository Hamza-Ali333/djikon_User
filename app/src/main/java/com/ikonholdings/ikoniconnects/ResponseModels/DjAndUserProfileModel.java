package com.ikonholdings.ikoniconnects.ResponseModels;


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
    private Boolean password;

    private int allow_message;
    private int allow_booking;

    public int getAllow_message() {
        return allow_message;
    }

    public int getAllow_booking() {
        return allow_booking;
    }

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

    public Boolean getPassword() {
        return password;
    }
}
