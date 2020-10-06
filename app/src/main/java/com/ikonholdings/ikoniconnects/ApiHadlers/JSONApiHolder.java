package com.ikonholdings.ikoniconnects.ApiHadlers;

import com.ikonholdings.ikoniconnects.ResponseModels.AllArtistModel;
import com.ikonholdings.ikoniconnects.ResponseModels.BookingHistory;
import com.ikonholdings.ikoniconnects.ResponseModels.CurrentLiveArtistModel;
import com.ikonholdings.ikoniconnects.ResponseModels.DjAndUserProfileModel;
import com.ikonholdings.ikoniconnects.ResponseModels.FeedBlogModel;
import com.ikonholdings.ikoniconnects.ResponseModels.FramesModel;
import com.ikonholdings.ikoniconnects.ResponseModels.LoginRegistrationModel;
import com.ikonholdings.ikoniconnects.ResponseModels.RequestedSongsModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SingleBlogDetailModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SingleServiceModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SubscribeArtistModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

    @GET("getStart")
    Call<SuccessErrorModel> getBrainTreeToken();

    //will return All subscribed Artist  by current User
    @GET ("following")
    Call<List<SubscribeArtistModel>> getSubscribeArtist();

    //will return All subscribed Artist  by current User
    @GET ("artistAll")
    Call<List<AllArtistModel>> getAllArtist();

    //will return all the requested Song
    @GET ("requested_songs")
    Call<List<RequestedSongsModel>> getRequestedSongs();

    //will return detail of a blog
    @GET
    Call<SingleBlogDetailModel> getSingleBlog(@Url String id);

    //this will return full detail subscriber profile
    //same for  current user profile
    @GET
    Call<DjAndUserProfileModel> getSubscriberOrUserProfile(@Url String id);

    //this will return full detail of a service
    @GET
    Call<SingleServiceModel> getSingleServiceData(@Url String id);

    //this will return current UserAll the booking
    @GET("bookingHistory")
    Call<List<BookingHistory>> getBookingHistory();

    @GET("liveArtist")
    Call<List<CurrentLiveArtistModel>> getCurrentLiveArtist();

    @GET("socialFrames")
    Call<List<FramesModel>>getFrames();

    @FormUrlEncoded
    @POST("register")
    Call <LoginRegistrationModel> registerUser(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("refferal") String refferal,
            @Field("role") int role
            );

    @FormUrlEncoded
    @POST("register")
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
    @POST("change_password")
    Call <SuccessErrorModel> changePasswrod(
            @Field("oldpass") String oldpassword,
            @Field("password") String newpassword
    );

    @FormUrlEncoded
    @POST("login")
    Call <LoginRegistrationModel> Login(
            @FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("chekout")
    Call <Void> Checkout(
            @FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST("verify_email")
    Call <LoginRegistrationModel> verifyEmail(
            @Field("email") String email,
            @Field("otp") Integer otp
    );

    @FormUrlEncoded
    @POST("resend_otp")
    Call <SuccessErrorModel> resendOTP(
            @Field("email") String email
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
    Call <SuccessErrorModel> updatePassword(
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

    @POST("logout")
    Call <LoginRegistrationModel> logout();

    @FormUrlEncoded
    @POST("bookings")
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
    @POST("updateToken")
    Call<Void> postFCMTokenForWeb(
            @Field("token") String Token
    );

    @FormUrlEncoded
    @POST("referralConfirm")
    Call<LoginRegistrationModel> postReferral(
            @Field("refferal") String Code,
            @Field("email") String Email
    );

}