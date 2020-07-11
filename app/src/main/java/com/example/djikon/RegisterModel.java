package com.example.djikon;

public class RegisterModel {
    private String
    firstname,
    lastname,
     email,
     password,
    c_password,
    refferal,
    message;

    private int role = 1;

    public RegisterModel(String firstname, String lastname, String email, String password, String c_password,String refferal, int role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.c_password = c_password;
        this.refferal = refferal;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public String getC_password() {
        return c_password;
    }

    public String getRefferal() {
        return refferal;
    }

    public int getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}
