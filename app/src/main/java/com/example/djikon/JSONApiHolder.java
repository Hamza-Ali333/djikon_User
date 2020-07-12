package com.example.djikon;

import android.media.session.MediaSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface JSONApiHolder {

    @GET ("blog")
    Call<List<Blog_Model>> getBlogs();

    @GET
    Call<SingleBlog_Model> getSingleBlog(@Url String url);

    @FormUrlEncoded
    @POST("register")
    Call <GetToken> registerUser(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("c_password") String c_password,
            @Field("refferal") String refferal,
            @Field("role") int role
            );



    @FormUrlEncoded
    @POST("login")
    Call <LoginModel> Login(
            @Field("email") String email,
            @Field("password") String password
    );
}


