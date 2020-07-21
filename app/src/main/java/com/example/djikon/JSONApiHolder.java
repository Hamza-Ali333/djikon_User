package com.example.djikon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface JSONApiHolder {

    //will return all the blogs
    @GET ("blog")
    Call<List<feed_Blog_Model>> getBlogs();

    //will return detail of a blog
    @GET
    Call<SingleBlog_Model> getSingleBlog(@Url String id);

    //this will return full detail dj profile
    @GET
    Call<DJProfileModel> getDjProfile(@Url String id);


    //this will return full detail of a service
    @GET
    Call<SingleServiceModle> getSingleServieData(@Url String id);

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
    @POST()
    Call<SuccessToken> postComment(@Url String blogId,
    @Field("body") String body
    );


    @FormUrlEncoded
    @POST("login")
    Call <SuccessToken> Login(
            @Field("email") String email,
            @Field("password") String password
    );



    @POST("logout")
    Call <SuccessToken> logout();
}


