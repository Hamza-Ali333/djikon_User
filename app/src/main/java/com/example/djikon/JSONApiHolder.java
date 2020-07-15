package com.example.djikon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface JSONApiHolder {

    @GET ("blog")
    Call<List<Blog_Model>> getBlogs();

    @GET
    Call<SingleBlog_Model> getSingleBlog(@Url String url);

    @FormUrlEncoded
    @POST("register")
    Call <SuccessToken> registerUser(
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
    Call <SuccessToken> Login(
            @Field("email") String email,
            @Field("password") String password
    );


//    @Headers("Authorization: 123")
    @POST("logout")
    Call <SuccessToken> logout();
}


