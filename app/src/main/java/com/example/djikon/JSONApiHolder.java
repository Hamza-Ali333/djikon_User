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
    Call<List<Feed_Blog_Model>> getBlogs();

    //will return detail of a blog
    @GET
    Call<SingleBlog_Model> getSingleBlog(@Url String id);

    //this will return full detail dj profile
    //same for user current user profile
    @GET
    Call<ProfileModel> getDjOrUserProfile(@Url String id);


    //this will return full detail of a service
    @GET
    Call<SingleServiceModle> getSingleServieData(@Url String id);

    @FormUrlEncoded
    @POST("register")
    Call <LoginRegistrationModel> registerUser(
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
    Call <SuccessErrorModel> UpdateUserProfile(
            @Url String userid,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("contact") String contact,
            @Field("gender") String gender,
            @Field("address") String address,
            @Field("location") String location
    );


    @FormUrlEncoded
    @POST()
    Call<SuccessErrorModel> postComment(@Url String blogId,
                                             @Field("body") String body
    );


    @FormUrlEncoded
    @POST("login")
    Call <SuccessErrorModel> updatePassword(
            @Field("oldpassword") String oldpassword,
            @Field("newpassword") String newpassword,
            @Field("role") String againpassword
    );


    @FormUrlEncoded
    @POST("login")
    Call <LoginRegistrationModel> Login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") Integer role
    );

    @FormUrlEncoded
    @POST("send_otp")
    Call <SuccessErrorModel> sendOTP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("confirm_otp")
    Call <SuccessErrorModel>  confirmOTP(
            @Field("email") String email,
            @Field("otp") Integer otp

    );

    @FormUrlEncoded
    @POST("update_password")
    Call <SuccessErrorModel>  updatePassword(
            @Field("email") String email,
            @Field("password") String newpassord
    );

    @FormUrlEncoded
    @POST()
    Call <SuccessErrorModel>  LikeUnlike(
            @Url String blogid,
            @Field("status") Integer likeStatus

    );


    @POST("logout")
    Call <LoginRegistrationModel> logout();
}


