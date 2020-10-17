package com.ikonholdings.ikoniconnects;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.Chat.ChatViewerActivity;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.FollowUnFollowArtist;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.ResponseModels.DjAndUserProfileModel;
import com.ikonholdings.ikoniconnects.ResponseModels.DjProfileBlogsModel;
import com.ikonholdings.ikoniconnects.ResponseModels.ServicesModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerDjBlogs;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DjProfileActivity extends AppCompatActivity implements FollowUnFollowArtist.FollowResultInterface {

    private Button btn_Book_Artist,
            btn_Request_A_Song,
            btn_Follow, btn_Message;

    private TextView
            txt_Subscriber_Name,
            txt_address,
            txt_Total_Follower,
            txt_about,
            txt_Subscriber_Rate,
            onlineStatus;

    private ScrollView parenLayout;

    private CircularImageView img_Subscriber_Profile;
    private ProgressBar profileBar;

    private AlertDialog loadingDialog;

    private String mSubscriberName,
            mAddress,
            mProfile,
            mAbout,
            mOnlineStatus,
            SubscriberBookingRatePerHour;

    private int artistID, mFollower_Count, mFollow_Status;

    private RecyclerView mServicesRecycler, mBlogRecyclerView;
    private RecyclerView.Adapter serviceAdapter;
    private RecyclerView.LayoutManager serviceLayoutManager;

    private RecyclerView.Adapter BlogAdapter;
    private RecyclerView.LayoutManager BlogLayoutManager;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;
    private ProgressDialog progressDialog;

    private int allowMessage;
    private int allowBooking;

    private List<ServicesModel> services;
    private List<DjProfileBlogsModel> blogs;

    private Snackbar snackbar;
    private TextView snackBarText;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private ValueEventListener listener;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_profile);
        getSupportActionBar().setTitle("Subscriber Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReferences();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        snackbar = Snackbar.make(parenLayout,"",Snackbar.LENGTH_LONG);
        snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(Color.YELLOW);

        parenLayout.setVisibility(View.GONE);//hide the parent view

        services = new ArrayList<ServicesModel>();//service Array Initializing
        Intent i = getIntent();
        artistID = i.getIntExtra("id", 0);

        showLoadingDialogue();//show lodaing dailoge while data is dowloading from the server

        getProfileDataFromServer(String.valueOf(artistID));//getAll the data of this user

        btn_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Follow.setClickable(false);
                btn_Follow.setEnabled(false);
                //followUnFollow();
                new FollowUnFollowArtist(
                        mFollow_Status,
                        String.valueOf(artistID),
                        DjProfileActivity.this).execute();
            }
        });

        btn_Book_Artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allowBooking == 1){
                    if(!SubscriberBookingRatePerHour.equals("no")){
                        addExtraToIntentAndLunchActivity(img_Subscriber_Profile,
                                artistID,
                                true,
                                false,
                                0,
                                "rate_per_hour",
                                SubscriberBookingRatePerHour,
                                mSubscriberName,
                                mAbout
                        );
                    }else {
                        DialogsUtils.showAlertDialog(DjProfileActivity.this,
                                false,
                                "Note!","This Subscriber is not set it's charge amount yet");
                    }
                }else {
                    DialogsUtils.showAlertDialog(DjProfileActivity.this,
                            false,
                            "Disable",
                            "This subscriber is not available for booking at this time try again!");
                }
            }
        });

        btn_Request_A_Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRequestASongDialogue();
            }
        });

        btn_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allowMessage == 1){
                    btn_Message.setClickable(false);
                    btn_Message.setEnabled(false);

                    lunchMessageActivity();
                }else {
                    DialogsUtils.showAlertDialog(DjProfileActivity.this,
                            false,
                            "Disable",
                            "This Subscriber is not allowed to do Chat with other wait for turn it on\nThank You");
                }
            }
        });
    }

    private void setDataInToViews() {
        txt_Subscriber_Name.setText(mSubscriberName);
        txt_Total_Follower.setText(String.valueOf(mFollower_Count));
        txt_Subscriber_Rate.setText(SubscriberBookingRatePerHour);

        if (mFollow_Status == 0) {
            btn_Follow.setText("Follow");
        } else {
            btn_Follow.setText("UnFollow");
        }

        txt_address.setText(mAddress);
        txt_about.setText(mAbout);
        txt_Total_Follower.setText(String.valueOf(mFollower_Count));

        if (mProfile!= null && !mProfile.equals("no")) {
            profileBar.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url+ mProfile)
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(img_Subscriber_Profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            profileBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError(Exception e) {
                            profileBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void buildServiceRecycler(List<ServicesModel> serviceList) {

        mServicesRecycler.setHasFixedSize(true);//if the recycler view not increase run time
        serviceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        serviceAdapter = new RecyclerServices(serviceList,artistID,this);

        mServicesRecycler.setLayoutManager(serviceLayoutManager);
        mServicesRecycler.setAdapter(serviceAdapter);

    }

    private void buildBlogRecycler(List<DjProfileBlogsModel> blogslist) {
        mBlogRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        BlogLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        BlogAdapter = new RecyclerDjBlogs(blogslist);

        mBlogRecyclerView.setLayoutManager(BlogLayoutManager);
        mBlogRecyclerView.setAdapter(BlogAdapter);
    }

    private void openRequestASongDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailoge_request_song, null);

        EditText edt_Song_Name = view.findViewById(R.id.edt_Song_Name);
        EditText edt_Requester_Name = view.findViewById(R.id.edt_requester_Name);
        Button btn_Submit = view.findViewById(R.id.btn_submit);

        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.show();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_Requester_Name.getText().toString().isEmpty() && !edt_Song_Name.getText().toString().isEmpty()) {

                    progressDialog = DialogsUtils.showProgressDialog(DjProfileActivity.this,"Posting Request","Please Wait...");
                    postRequestSong(edt_Requester_Name.getText().toString(), edt_Song_Name.getText().toString());
                    alertDialog.dismiss();
                }else {
                    if(edt_Requester_Name.getText().toString().isEmpty()){
                        edt_Requester_Name.setError("Please enter your name");
                    }else {
                        edt_Song_Name.setError("Please enter song name");
                    }
                }
            }
        });
    }

    private void getProfileDataFromServer(String subscriberId) {
        retrofit= ApiClient.retrofit(this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        String relativeURL = "user/"+subscriberId;
        Call<DjAndUserProfileModel> call = jsonApiHolder.getSubscriberOrUserProfile(relativeURL);

        call.enqueue(new Callback<DjAndUserProfileModel>() {
            @Override
            public void onResponse(Call<DjAndUserProfileModel> call, Response<DjAndUserProfileModel> response) {

                if (response.isSuccessful()) {
                    DjAndUserProfileModel djAndUserProfileModel = response.body();
                    mSubscriberName = djAndUserProfileModel.getFirstname() + " " + response.body().getLastname();
                    mAddress = djAndUserProfileModel.getLocation();
                    mProfile = djAndUserProfileModel.getProfile_image();
                    mFollower_Count = djAndUserProfileModel.getFollowers();
                    mFollow_Status = djAndUserProfileModel.getFollow_status();
                    mOnlineStatus = djAndUserProfileModel.getOnline_status();
                    mAbout = djAndUserProfileModel.getAbout();
                    allowBooking = djAndUserProfileModel.getAllow_booking();
                    allowMessage = djAndUserProfileModel.getAllow_message();


                    services = djAndUserProfileModel.getServices();
                    blogs = djAndUserProfileModel.getBlog();
                    SubscriberBookingRatePerHour = djAndUserProfileModel.getRate_per_hour();

                    parenLayout.setVisibility(View.VISIBLE);
                    loadingDialog.dismiss();//hide the loading Dailoge

                    buildServiceRecycler(services);
                    buildBlogRecycler(blogs);


                    setDataInToViews();//set User Data in to Views

                    setOnlineStatus(mOnlineStatus);//checkUser is Online or Not

                } else {
                    loadingDialog.dismiss();
                    Log.i("TAG", "onResponse: " + response.code());
                    DialogsUtils.showResponseMsg(DjProfileActivity.this,false);

                    return;
                }
            }

            @Override
            public void onFailure(Call<DjAndUserProfileModel> call, Throwable t) {
                loadingDialog.dismiss();
                DialogsUtils.showResponseMsg(DjProfileActivity.this,true);
            }

        });

    }

    private void setOnlineStatus(String onlineStatus) {
        if(onlineStatus != null) {
            if (onlineStatus.equals("0")) {
                this.onlineStatus.setText("Offline");
                this.onlineStatus.setTextColor(getResources().getColor(R.color.colorRed));
                this.onlineStatus.setBackgroundResource(R.drawable.redround_stroke);
            } else {
                this.onlineStatus.setText("Online ");
                this.onlineStatus.setTextColor(getResources().getColor(R.color.colorBlue));
                this.onlineStatus.setBackgroundResource(R.drawable.blueround_stroke);
            }
        }
    }

    private void createReferences() {
        parenLayout = findViewById(R.id.scrollable);
        btn_Book_Artist = findViewById(R.id.btn_book_artist);
        btn_Request_A_Song = findViewById(R.id.btn_RequestASong);
        btn_Follow = findViewById(R.id.Follow_Dj);
        btn_Message = findViewById(R.id.message_dj);

        onlineStatus = findViewById(R.id.txt_dj_status);
        txt_Subscriber_Rate = findViewById(R.id.djRate);
        txt_Subscriber_Name = findViewById(R.id.txt_subscriber_name);
        txt_address = findViewById(R.id.txt_address);
        txt_Total_Follower = findViewById(R.id.txt_followers);
        txt_about = findViewById(R.id.txt_about_dj);

        mServicesRecycler = findViewById(R.id.services_recycler);
        mBlogRecyclerView = findViewById(R.id.blog_recyclerview);

        img_Subscriber_Profile = findViewById(R.id.img_subscriber_profile);
        profileBar = findViewById(R.id.progressBarProfile);

    }

    private void postRequestSong (String UserName,String SongName) {
        retrofit= ApiClient.retrofit(this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        String relativeUrl = "request_song/"+String.valueOf(artistID);
        Call<SuccessErrorModel> call = jsonApiHolder.postSongRequest(relativeUrl,
                UserName,
                SongName
        );

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    snackBarText.setText("Request Posted Successfully");
                    snackbar.show();
                }else {
                    progressDialog.dismiss();
                    DialogsUtils.showResponseMsg(DjProfileActivity.this,false);
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                snackBarText.setText("OPPS Something Happend Wrong Check Network");
                snackbar.show();
                DialogsUtils.showResponseMsg(DjProfileActivity.this,true);
            }
        });
    }


    private void followUnFollow (Boolean responseResult) {
        if(responseResult){
            if (mFollow_Status == 0){
                mFollow_Status = 1;
                mFollower_Count++;
                snackBarText.setText(" Follow Successfully");
                btn_Follow.setText("UnFollow");
            }
            else {
                mFollow_Status = 0;
                mFollower_Count--;
                snackBarText.setText(" UnFollow Successfully");
                btn_Follow.setText("Follow");
            }
            txt_Total_Follower.setText(String.valueOf(mFollower_Count));
            snackbar.show();
            btn_Follow.setClickable(true);
            btn_Follow.setEnabled(true);
        }else {
            DialogsUtils.showResponseMsg(DjProfileActivity.this,
                    false);
        }
    }

    private void showLoadingDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialoge_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        loadingDialog = builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void followResponse(Boolean response) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                followUnFollow(response);
            }
        });

    }

    private void lunchMessageActivity() {
        img_Subscriber_Profile.buildDrawingCache();
        Bitmap bitmap = img_Subscriber_Profile.getDrawingCache();
        Intent i = new Intent(DjProfileActivity.this, ChatViewerActivity.class);
        i.putExtra("subscriber_Id", String.valueOf(artistID));
        i.putExtra("subscriber_Name",mSubscriberName);
        i.putExtra("imgProfileUrl",mProfile);
        startActivity(i);
    }

    private void addExtraToIntentAndLunchActivity (ImageView img,
                                                   int artistId,
                                                   Boolean bookingForArtist,
                                                   Boolean bookingForService,
                                                   int serviceId,
                                                   String priceType,
                                                   String price,
                                                   String serviceNameOrSubscriberName,
                                                   String description) {

        img.buildDrawingCache();
        Bitmap bitmap = img.getDrawingCache();
        Intent i = new Intent(DjProfileActivity.this, BookArtistOrServiceActivity.class);
        i.putExtra("artistId",artistId);
        i.putExtra("bookingForArtist", bookingForArtist);//booking Artist true
        i.putExtra("bookingForService", bookingForService);//booking Service true
        i.putExtra("serviceId",serviceId);
        i.putExtra("priceType", priceType);
        i.putExtra("BitmapImage", bitmap);
        i.putExtra("price", price);//rate per hour
        i.putExtra("serviceOrSubscriberName", serviceNameOrSubscriberName);//Name of Artist if booking him or Name of Service if Booking Artist Service
        i.putExtra("description", description);
        startActivity(i);
    }
}