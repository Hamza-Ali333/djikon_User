package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.BrainTree.BrainTreeActivity;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.Models.DjAndUserProfileModel;
import com.example.djikon.Models.DjProfileBlogsModel;
import com.example.djikon.Models.ServicesModel;
import com.example.djikon.Models.SuccessErrorModel;
import com.example.djikon.RecyclerView.RecyclerDjBlogs;
import com.example.djikon.RecyclerView.RecyclerServices;
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

public class DjProfileActivity extends AppCompatActivity {

    private Button btn_Book_Artist,
            btn_Request_A_Song,
            btn_Message,
            btn_Follow;

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


    List<ServicesModel> services;
    List<DjProfileBlogsModel> blogs;

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
        createRefrences();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        snackbar = Snackbar.make(parenLayout,"",Snackbar.LENGTH_LONG);
        snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(Color.YELLOW);

        parenLayout.setVisibility(View.GONE);//hide the parent view

        showLoadingDialogue();//show lodaing dailoge while data is dowloading from the server

        services = new ArrayList<ServicesModel>();
        Intent i = getIntent();
        int blogId = i.getIntExtra("id", 0);


        getProfileDataFromServer(String.valueOf(blogId));


        btn_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Follow.setClickable(false);
                btn_Follow.setEnabled(false);
                if(mFollow_Status== 0){
                    mFollower_Count--;
                    followUnFollow(0);
                }else {
                    mFollower_Count++;
                    followUnFollow(1);
                }
                txt_Total_Follower.setText(String.valueOf(mFollower_Count));
            }
        });


        btn_Book_Artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DjProfileActivity.this, BrainTreeActivity.class));

//                img_DJ_Profile.buildDrawingCache();
//                Bitmap bitmap = img_DJ_Profile.getDrawingCache();
//                Intent i = new Intent(DjProfileActivity.this, BookArtistOrServiceActivity.class);
//                i.putExtra("id",String.valueOf(artistID));
//                i.putExtra("BitmapImage", bitmap);
//                i.putExtra("price",DjBookingRatePerHour);//rate per hour
//                i.putExtra("name",mDJName);
//                i.putExtra("request_code", 1);//one for dj booking
//                i.putExtra("description",mAbout);
//                startActivity(i);
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
                if(artist_UID != null){
                    lunchMessageActivity();
                }else {
                    new GetDjUidFromFirebase().execute();
                }
            }
        });
    }

    private void getDjUid() {
        myRef= FirebaseDatabase.getInstance().getReference("All_Users");
        myRef.child("DJs").child(String.valueOf(artistID)).addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    artist_UID = dataSnapshot.child("uid").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        if (!mProfile.equals("no")) {
            Picasso.get().load(mProfile)
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
        serviceAdapter = new RecyclerServices(serviceList);

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

    private void getProfileDataFromServer(String blogId) {
         retrofit= ApiClient.retrofit(this);
         jsonApiHolder = retrofit.create(JSONApiHolder.class);
         String relativeURL = "api/user/"+blogId;
         Call<DjAndUserProfileModel> call = jsonApiHolder.getDjOrUserProfile(relativeURL);

        call.enqueue(new Callback<DjAndUserProfileModel>() {
            @Override
            public void onResponse(Call<DjAndUserProfileModel> call, Response<DjAndUserProfileModel> response) {

                if (response.isSuccessful()) {
                    DjAndUserProfileModel djAndUserProfileModel = response.body();
                    artistID = djAndUserProfileModel.getId();
                    mDJName = djAndUserProfileModel.getFirstname() + " " + response.body().getLastname();
                    mAddress = djAndUserProfileModel.getLocation();
                    mProfile = djAndUserProfileModel.getProfile_image();
                    mFollower_Count = djAndUserProfileModel.getFollowers();
                    mFollow_Status = djAndUserProfileModel.getFollow_status();
                    mOnlineStatus = djAndUserProfileModel.getOnline_status();
                    mAbout = djAndUserProfileModel.getAbout();


                    services = response.body().getServices();
                    blogs = response.body().getBlog();
                    DjBookingRatePerHour = response.body().getRate_per_hour();

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

                    new GetDjUidFromFirebase().execute();//getUid of this user from firebase for massageing Use
                } else {

                    Log.i("TAG", "onResponse: " + response.code());

                    return;
                }
            }

            @Override
            public void onFailure(Call<DjAndUserProfileModel> call, Throwable t) {

                Log.i("TAG", "onFailure: " + t.getMessage());
                alertDialog = DialogsUtils.showAlertDialog(DjProfileActivity.this,false,"No Internet","Please Check Your Internet Connection");
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

    private void createRefrences() {

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
                    snackBarText.setText("OPPS Request Failed To Posted Try Again");
                    snackbar.show();

                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                progressDialog.dismiss();
                snackBarText.setText("OPPS Something Happend Wrong Check Network");
                snackbar.show();
                alertDialog = DialogsUtils.showAlertDialog(DjProfileActivity.this,false,"No Internet","Please Check Your Internet Connection");
            }
        });
    }


    private void followUnFollow (int CurrentStatus) {
        Retrofit retrofit = ApiClient.retrofit( DjProfileActivity.this);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        String relativeUrl = "";
        //0 means not following yet
        if(CurrentStatus == 0){
            relativeUrl = "api/follow_artist/"+String.valueOf(artistID);

        }else {
            relativeUrl = "api/unfollow_artist/"+String.valueOf(artistID);
        }

        Call <SuccessErrorModel> call = jsonApiHolder.followUnFollowArtist(relativeUrl);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    if (CurrentStatus == 0){
                        snackBarText.setText(" Follow Successfully");
                        mFollow_Status = 1;
                        mFollower_Count++;
                        btn_Follow.setText("UnFollow");
                    }
                    else {
                        mFollow_Status = 0;
                        mFollower_Count--;
                        snackBarText.setText(" UnFollow Successfully");
                        btn_Follow.setText("Follow");
                    }

                    snackbar.show();
                    btn_Follow.setClickable(false);
                    btn_Follow.setEnabled(false);
                }else {
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {

                alertDialog = DialogsUtils.showAlertDialog(DjProfileActivity.this,false,"No Internet","Please Check Your Internet Connection");
            }
        });
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

    private class GetDjUidFromFirebase extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            getDjUid();
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
        i.putExtra("id",artistID);
        i.putExtra("uid",artist_UID);
        i.putExtra("djName",mDJName);
        i.putExtra("imgProfileUrl",mProfile);
        startActivity(i);
    }
}