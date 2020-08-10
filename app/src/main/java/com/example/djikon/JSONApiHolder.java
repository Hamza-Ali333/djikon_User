package com.example.djikon;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface JSONApiHolder {

    //will return all the blogs
    @GET ("blog")
    Call<List<FeedBlogModel>> getBlogs();

    //will return All subscribed Aritist
    @GET ("following")
    Call<List<SubscribeArtistModel>> getSubscribeArtist();

    //will return all the requested Song
    @GET ("requested_songs")
    Call<List<RequestedSongsModel>> getRequestedSongs();

    //will return detail of a blog
    @GET
    Call<SingleBlogDetailModel> getSingleBlog(@Url String id);

    //this will return full detail dj profile
    //same for user current user profile
    @GET
    Call<DjAndUserProfileModel> getDjOrUserProfile(@Url String id);


    //this will return full detail of a service
    @GET
    Call<SingleServiceModel> getSingleServieData(@Url String id);

    @FormUrlEncoded
    @POST("register")
    Call <SuccessErrorModel> registerUser(
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
            @Field("image") String image,
            @Field("firstname") String  firstname,
            @Field("lastname") String  lastname,
            @Field("contact") String  contact,
            @Field("gender") String  gender,
            @Field("location") String  location
    );


        @Multipart
        @POST()
        Call<SuccessErrorModel> uploadImage(
                @Url String userid,
                @Part MultipartBody.Part image
        );




    @FormUrlEncoded
    @POST()
    Call<SuccessErrorModel> postComment(@Url String blogId,
                                             @Field("body") String body
    );


    @FormUrlEncoded
    @POST("change_password")
    Call <SuccessErrorModel> changepasswrod(
            @Field("oldpass") String oldpassword,
            @Field("password") String newpassword
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
    @POST("resend_otp")
    Call <SuccessErrorModel> resendOTP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("verify_email")
    Call <LoginRegistrationModel> verifyEmail(
            @Field("email") String email,
            @Field("otp") Integer otp
    );


    @FormUrlEncoded
    @POST("confirm_otp")
    Call <SuccessErrorModel>  confirmOTP(
            @Field("email") String email,
            @Field("otp") Integer otp

    );

    @FormUrlEncoded
    @POST("update_password")
    Call <SuccessErrorModel> Updatepassword(
            @Field("email") String email,
            @Field("password") String newpassord
    );

    @FormUrlEncoded
    @POST()
    Call <SuccessErrorModel>  LikeUnlike(
            @Url String blogid,
            @Field("status") Integer likeStatus

    );


    @POST()
    Call <SuccessErrorModel>  followUnFollowArtist(
            @Url String artistId
    );

    @POST("logout")
    Call <LoginRegistrationModel> logout();

    @FormUrlEncoded
    @POST()
    Call<SuccessErrorModel> postBooking(
            @Url String id,
            @Field("name") String Name,
            @Field("email") String Email,
            @Field("phone") String Phone,
            @Field("address") String Address,
            @Field("start_date") String Start_Date,
            @Field("end_date") String End_Date,
            @Field("start_time") String Start_Time,
            @Field("end_time") String   End_Time,
            @Field("price") String   PaidAmount
    );

    @FormUrlEncoded
    @POST()
    Call<SuccessErrorModel> postSongRequest(
            @Url String id,
            @Field("name") String your_Name,
            @Field("song_name") String Song_Name
    );

}


