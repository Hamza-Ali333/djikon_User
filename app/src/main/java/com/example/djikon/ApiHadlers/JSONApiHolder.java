package com.example.djikon.ApiHadlers;

import com.example.djikon.ResponseModels.AllArtistModel;
import com.example.djikon.ResponseModels.BookingHistory;
import com.example.djikon.ResponseModels.DjAndUserProfileModel;
import com.example.djikon.ResponseModels.FeedBlogModel;
import com.example.djikon.ResponseModels.LoginRegistrationModel;
import com.example.djikon.ResponseModels.RequestedSongsModel;
import com.example.djikon.ResponseModels.SingleBlogDetailModel;
import com.example.djikon.ResponseModels.SingleServiceModel;
import com.example.djikon.ResponseModels.SubscribeArtistModel;
import com.example.djikon.ResponseModels.SuccessErrorModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @GET ("api/blog")
    Call<List<FeedBlogModel>> getBlogs();

    @GET("api/getStart")
    Call<SuccessErrorModel> getBrainTreeToken();

    //will return All subscribed Artist  by current User
    @GET ("api/following")
    Call<List<SubscribeArtistModel>> getSubscribeArtist();

    //will return All subscribed Artist  by current User
    @GET ("api/artistAll")
    Call<List<AllArtistModel>> getAllArtist();

    //will return all the requested Song
    @GET ("api/requested_songs")
    Call<List<RequestedSongsModel>> getRequestedSongs();

    //will return detail of a blog
    @GET
    Call<SingleBlogDetailModel> getSingleBlog(@Url String id);

    //this will return full detail dj profile
    //same for  current user profile
    @GET
    Call<DjAndUserProfileModel> getDjOrUserProfile(@Url String id);

    //this will return full detail of a service
    @GET
    Call<SingleServiceModel> getSingleServieData(@Url String id);

    //this will return current UserAll the booking
    @GET("api/bookingHistory")
    Call<List<BookingHistory>> getBookingHistory();

    @FormUrlEncoded
    @POST("api/register")
    Call <LoginRegistrationModel> registerUser(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("refferal") String refferal,
            @Field("role") int role
            );

    @FormUrlEncoded
    @POST("api/register")
    Call <LoginRegistrationModel> registerUserFromSocialMedia(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("social") int social,
            @Field("role") int role
    );

        @Multipart
        @POST()
        Call<SuccessErrorModel> UpdateProfileWithImage(
                @Url String relativeUrl,
                @Part MultipartBody.Part image,
                @Part("firstname") RequestBody firstName,
                @Part("lastname")  RequestBody lastName,
                @Part("contact")   RequestBody contact,
                @Part("gender")    RequestBody gender,
                @Part("location")  RequestBody location
        );

    @FormUrlEncoded
    @POST()
    Call<SuccessErrorModel> postComment(@Url String relativeUrl,
                                             @Field("body") String body
    );


    @FormUrlEncoded
    @POST("api/change_password")
    Call <SuccessErrorModel> changePasswrod(
            @Field("oldpass") String oldpassword,
            @Field("password") String newpassword
    );


    @FormUrlEncoded
    @POST("api/login")
    Call <LoginRegistrationModel> Login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") Integer role
    );



    @FormUrlEncoded
    @POST("api/resend_otp")
    Call <SuccessErrorModel> resendOTP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/verify_email")
    Call <LoginRegistrationModel> verifyEmail(
            @Field("email") String email,
            @Field("otp") Integer otp
    );

    
    @FormUrlEncoded
    @POST("api/send_otp")
    Call <SuccessErrorModel> sendOTP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/confirm_otp")
    Call <SuccessErrorModel>  confirmOTP(
            @Field("email") String email,
            @Field("otp") Integer otp

    );

    @FormUrlEncoded
    @POST("api/update_password")
    Call <SuccessErrorModel> Updatepassword(
            @Field("email") String email,
            @Field("password") String newpassord
    );

    @FormUrlEncoded
    @POST()
    Call <SuccessErrorModel>  LikeUnlike(
            @Url String relativeUrl,
            @Field("status") Integer likeStatus
    );

    @POST()
    Call <SuccessErrorModel>  followUnFollowArtist(
            @Url String relativeUrl
    );

    @POST("api/logout")
    Call <LoginRegistrationModel> logout();

    @FormUrlEncoded
    @POST("api/bookings")
    Call<SuccessErrorModel> postBooking(
            @Field("sub_id") int artistId,
            @Field("service_id") int ServiceId,
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
            @Url String relativeUrl,
            @Field("name") String your_Name,
            @Field("song_name") String Song_Name
    );

    @FormUrlEncoded
    @POST("api/updateToken")
    Call<Void> postFCMTokenForWeb(
            @Field("token") String Token
    );

}


