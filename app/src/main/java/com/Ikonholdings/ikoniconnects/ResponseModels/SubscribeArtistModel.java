package com.Ikonholdings.ikoniconnects.ResponseModels;

public class SubscribeArtistModel {

    private int id;

    private String firstname,
    lastname,
    profile_image,
    location;

    private boolean checkState = false;

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getLocation() {
        return location;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }

    public boolean getCheckState() {
        return checkState;
    }
}
