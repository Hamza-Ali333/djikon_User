package com.example.djikon;

public class LoginModel {
    private String email,password;


    public LoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
