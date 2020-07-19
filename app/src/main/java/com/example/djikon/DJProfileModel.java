package com.example.djikon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DJProfileModel {


    private Integer id;

    private String profile_image;

    private String firstname;

    private String lastname;

    @SerializedName("address")
    @Expose
    private String address;

    private List<Blog_Model> blog;

    private List<Services_Model> services;

    /**
     * No args constructor for use in serialization
     *
     * @param body
     */
    public DJProfileModel(DJProfileModel body) {
    }


    public DJProfileModel(Integer id, String profileImage, String firstname, String lastname, String address, List<Blog_Model> blog, List<Services_Model> services) {
        super();
        this.id = id;
        this.profile_image = profileImage;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.blog = blog;
        this.services = services;
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

    public String getAddress() {
        return address;
    }

    public List<Blog_Model> getBlog() {
        return blog;
    }

    public List<Services_Model> getServices() {
        return services;
    }
}
