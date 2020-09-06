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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.FollowResultInterface;
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

public class DjProfileActivity extends AppCompatActivity implements FollowResultInterface {

    private Button btn_Book_Artist,
            btn_Request_A_Song,
            btn_Follow, btn_Message;

    private TextView
            txt_DJ_Name,
            txt_address,
            txt_Total_Follower,
            txt_about,
            txt_Progress_Msg,
            onlineStatus;

    private ScrollView parenLayout;
    private RelativeLayout rlt_About;

    private CircularImageView img_DJ_Profile;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private String mDJName,
            mAddress,
            mProfile,
            mAbout,
            mOnlineStatus,
    DjBookingRatePerHour;

    private int artistID, mFollower_Count, mFollow_Status;

    private RecyclerView mServicesRecycler, mBlogRecyclerView;
    private RecyclerView.Adapter serviceAdapter;
    private RecyclerView.LayoutManager serviceLayoutManager;

    private RecyclerView.Adapter BlogAdapter;
    private RecyclerView.LayoutManager BlogLayoutManager;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;
    private ProgressDialog progressDialog;

    private String artist_UID;
    private int allowMessage;
    private int allowBooking;

    private List<ServicesModel> services;
    private List<DjProfileBlogsModel> blogs;

    private Snackbar snackbar;
    private TextView snackBarText;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private DatabaseReference myRef;
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
        getSupportActionBar().setTitle("Dj Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReferences();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        snackbar = Snackbar.make(parenLayout,"",Snackbar.LENGTH_LONG);
        snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(Color.YELLOW);

        parenLayout.setVisibility(View.GONE);//hide the parent view

        showLoadingDialogue();//show lodaing dailoge while data is dowloading from the server

        services = new ArrayList<ServicesModel>();//service Array Initializing
        Intent i = getIntent();
        artistID = i.getIntExtra("id", 0);

        getProfileDataFromServer(String.valueOf(artistID));//getAll the data of this user

        btn_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Follow.setClickable(false);
                btn_Follow.setEnabled(false);
                //followUnFollow();
                new FollowUnFollowArtist(1,
                        "1",
                        DjProfileActivity.this).execute();
            }
        });

        btn_Book_Artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allowBooking == 1){
                    addExtraToIntentAndLunchActivity(img_DJ_Profile,
                            artistID,
                            true,
                            false,
                            0,
                            "rate_per_hour",
                            DjBookingRatePerHour,
                            mDJName,
                            mAbout
                    );
                }else {
                    DialogsUtils.showAlertDialog(DjProfileActivity.this,
                            false,
                            "Disable",
                            "This dj is not available for booking at this time try again!");
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

                    new GetDjUidFromFirebase().execute();
                }else {
                    DialogsUtils.showAlertDialog(DjProfileActivity.this,
                            false,
                            "Disable",
                            "This DJ is not allowed to do Chat with other wait for turn it on\nThank You");
                }
            }
        });
    }

    private void setDataInToViews() {
        txt_DJ_Name.setText(mDJName);
        txt_Total_Follower.setText(String.valueOf(mFollower_Count));

        if (mFollow_Status == 0) {
            btn_Follow.setText("Follow");
        } else {
            btn_Follow.setText("UnFollow");
        }

        txt_address.setText(mAddress);

        if(mAbout == null)
            rlt_About.setVisibility(View.GONE);
        else {
            rlt_About.setVisibility(View.VISIBLE);
            txt_about.setText(mAbout);
        }
        txt_Total_Follower.setText(String.valueOf(mFollower_Count));

        if (mProfile!= null && !mProfile.equals("no")) {
            Picasso.get().load("http://ec2-52-91-44-156.compute-1.amazonaws.com/" + mProfile)
                    .placeholder(R.drawable.progressbar)
                    .fit()
                    .centerCrop()
                    .into(img_DJ_Profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(DjProfileActivity.this, "Something Happend Wrong feed image", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DjProfileActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getProfileDataFromServer(String djId) {
         retrofit= ApiClient.retrofit(this);
         jsonApiHolder = retrofit.create(JSONApiHolder.class);
         String relativeURL = "api/user/"+djId;
         Call<DjAndUserProfileModel> call = jsonApiHolder.getDjOrUserProfile(relativeURL);

         call.enqueue(new Callback<DjAndUserProfileModel>() {
            @Override
            public void onResponse(Call<DjAndUserProfileModel> call, Response<DjAndUserProfileModel> response) {

                if (response.isSuccessful()) {
                    DjAndUserProfileModel djAndUserProfileModel = response.body();
                    mDJName = djAndUserProfileModel.getFirstname() + " " + response.body().getLastname();
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
                    DjBookingRatePerHour = djAndUserProfileModel.getRate_per_hour();

                    parenLayout.setVisibility(View.VISIBLE);
                    alertDialog.dismiss();//hide the loading Dailoge

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            buildServiceRecycler(services);
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            buildBlogRecycler(blogs);
                        }
                    }).start();

                    setDataInToViews();//set User Data in to Views

                    setOnlineStatus(mOnlineStatus);//checkUser is Online or Not

                } else {
                    alertDialog.dismiss();
                    Log.i("TAG", "onResponse: " + response.code());
                    DialogsUtils.showResponseMsg(DjProfileActivity.this,false);

                    return;
                }
            }

            @Override
            public void onFailure(Call<DjAndUserProfileModel> call, Throwable t) {
                alertDialog.dismiss();
                Log.i("TAG", "onFailure: " + t.getMessage());
                alertDialog =DialogsUtils.showResponseMsg(DjProfileActivity.this,true);
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
        rlt_About = findViewById(R.id.rlt_about);
        btn_Book_Artist = findViewById(R.id.btn_book_artist);
        btn_Request_A_Song = findViewById(R.id.btn_RequestASong);
        btn_Follow = findViewById(R.id.Follow_Dj);
        btn_Message = findViewById(R.id.message_dj);

        onlineStatus = findViewById(R.id.txt_dj_status);
        txt_DJ_Name = findViewById(R.id.txt_dj_name);
        txt_address = findViewById(R.id.txt_address);
        txt_Total_Follower = findViewById(R.id.txt_followers);
        txt_about = findViewById(R.id.txt_about_dj);

        mServicesRecycler = findViewById(R.id.services_recycler);
        mBlogRecyclerView = findViewById(R.id.blog_recyclerview);

        img_DJ_Profile = findViewById(R.id.img_dj_profile);

    }

    private void postRequestSong (String UserName,String SongName) {
        retrofit= ApiClient.retrofit(this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        String relativeUrl = "api/request_song/"+String.valueOf(artistID);
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
                    alertDialog = DialogsUtils.showResponseMsg(DjProfileActivity.this,false);
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                progressDialog.dismiss();
                snackBarText.setText("OPPS Something Happend Wrong Check Network");
                snackbar.show();
                alertDialog = DialogsUtils.showResponseMsg(DjProfileActivity.this,true);
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
                    alertDialog = DialogsUtils.showResponseMsg(DjProfileActivity.this,
                            false);
                }
    }

    private void showLoadingDialogue() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialoge_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
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

    private class GetDjUidFromFirebase extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = DialogsUtils.showProgressDialog(DjProfileActivity.this,
                    "Getting Ready","Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myRef= FirebaseDatabase.getInstance().getReference("All_Users");

            myRef.child("DJs").child(String.valueOf(artistID)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                        artist_UID = dataSnapshot.child("uid").getValue(String.class);

                            lunchMessageActivity();

                        }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_Message.setClickable(true);
                                btn_Message.setEnabled(true);
                                progressDialog.dismiss();
                                alertDialog = DialogsUtils.showAlertDialog(DjProfileActivity.this,
                                        false, "Not Connected", "May currently this DJ is not able to get massage.\nor try again!");
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            btn_Message.setClickable(true);
                            btn_Message.setEnabled(true);
                            alertDialog = DialogsUtils.showAlertDialog(DjProfileActivity.this,
                                    false,"Error","Something happened wrong\nplease try again!");
                        }
                     });
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(artist_UID != null){
                lunchMessageActivity();
            }
        }
    }

    private void lunchMessageActivity() {
        Intent i = new Intent(DjProfileActivity.this,ChatViewerActivity.class);
        i.putExtra("dj_Id", String.valueOf(artistID));
        i.putExtra("dj_Uid",artist_UID);
        i.putExtra("dj_Name",mDJName);
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
                                                   String serviceNameOrDjName,
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
        i.putExtra("serviceOrDjName", serviceNameOrDjName);//Name of Artist if booking him or Name of Service if Booking Artist Service
        i.putExtra("description", description);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        parenLayout.setVisibility(View.GONE);
        //showLoadingDialogue();
        getProfileDataFromServer(String.valueOf(artistID));
    }
}