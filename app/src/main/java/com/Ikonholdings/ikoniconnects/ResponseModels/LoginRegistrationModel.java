package com.Ikonholdings.ikoniconnects.ResponseModels;

public class LoginRegistrationModel {

    private String success,
    firstname,
    lastname,
    profile_image,
    error;

    private int id;

    public String getError() {
        return error;
    }

    public int getId() {
        return id;
    }

    public String getSuccess() {
        return success;
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

}
