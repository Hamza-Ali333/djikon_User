package com.example.djikon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DjPrpfileActivity extends AppCompatActivity {

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


    List<Services_Model> services;
    List<Dj_Blogs_Model> blogs;

    private Snackbar snackbar;
    private TextView snackBarText;

    private final static String BASE_URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/user/";
    private final static String URL_REQUEST_SONG = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/request_song/";
    private final static String URL_FOLLOW_ARTIST = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/follow_artist/";
    private final static String URL_UN_FOLLOW_ARTIST = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/unfollow_artist/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_profile);
        getSupportActionBar().setTitle("Dj Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        snackbar = Snackbar.make(parenLayout,"",Snackbar.LENGTH_LONG);
        snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(Color.YELLOW);

        parenLayout.setVisibility(View.GONE);//hide the parent view

        showLoadingDialogue();//show lodaing dailoge while data is dowloading from the server

        services = new ArrayList<Services_Model>();
        Intent i = getIntent();
        int blogId = i.getIntExtra("id", 0);


        getProfileDataFromServer(String.valueOf(blogId));


        btn_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Follow.setClickable(false);
                btn_Follow.setEnabled(false);
                if(mFollow_Status== 0){
                    followUnFollow(URL_FOLLOW_ARTIST,0);
                }else {
                    followUnFollow(URL_UN_FOLLOW_ARTIST,1);
                }

            }
        });


        btn_Book_Artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_DJ_Profile.buildDrawingCache();
                Bitmap bitmap = img_DJ_Profile.getDrawingCache();
                Intent i = new Intent(DjPrpfileActivity.this, BookArtistActivity.class);
                i.putExtra("id",String.valueOf(artistID));
                i.putExtra("BitmapImage", bitmap);
                i.putExtra("price",DjBookingRatePerHour);//rate per hour
                i.putExtra("name",mDJName);
                i.putExtra("request_code", 1);//one for dj booking
                i.putExtra("description",mAbout);
                startActivity(i);
            }
        });


        btn_Request_A_Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRequestASongDialogue();
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
                            Toast.makeText(DjPrpfileActivity.this, "Something Happend Wrong feed image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void buildServiceRecycler(List<Services_Model> serviceList) {

        mServicesRecycler.setHasFixedSize(true);//if the recycler view not increase run time
        serviceLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        serviceAdapter = new RecyclerServices(serviceList);

        mServicesRecycler.setLayoutManager(serviceLayoutManager);
        mServicesRecycler.setAdapter(serviceAdapter);

    }

    private void buildBlogRecycler(List<Dj_Blogs_Model> blogslist) {

        mBlogRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        BlogLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        BlogAdapter = new RecyclerDjBlogs(blogslist);

        mBlogRecyclerView.setLayoutManager(BlogLayoutManager);
        mBlogRecyclerView.setAdapter(BlogAdapter);
    }

    private void openRequestASongDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dailogue_request_song, null);


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

                    progressDialog = DialogsUtils.showProgressDialog(DjPrpfileActivity.this,"Posting Request","Please Wait...");
                    postRequestSong(edt_Requester_Name.getText().toString(), edt_Song_Name.getText().toString());
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(DjPrpfileActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private void getProfileDataFromServer(String blogId) {

         retrofit= ApiResponse.retrofit(BASE_URL,this);

         jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<ProfileModel> call = jsonApiHolder.getDjOrUserProfile(blogId);

        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                if (response.isSuccessful()) {

                    Log.i("TAG", "onResponse: " + response.code());

                    ProfileModel profileModel = response.body();
                    artistID = profileModel.getId();
                    mDJName = profileModel.getFirstname() + " " + response.body().getLastname();
                    mAddress = profileModel.getLocation();
                    mProfile = profileModel.getProfile_image();
                    mFollower_Count = profileModel.getFollowers();
                    mFollow_Status = profileModel.getFollow_status();
                    mOnlineStatus = profileModel.getOnline_status();
                    mAbout = profileModel.getAbout();


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

                    setDataInToViews();

                    setOnlineStatus(mOnlineStatus);

                } else {

                    Log.i("TAG", "onResponse: " + response.code());

                    return;
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {

                Log.i("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(DjPrpfileActivity.this, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void setOnlineStatus(String onlineStatus) {
    if(onlineStatus.equals("0")){
        this.onlineStatus.setText("Offline");
        this.onlineStatus.setTextColor(getResources().getColor(R.color.colorRed));
        this.onlineStatus.setBackgroundResource(R.drawable.redround_stroke);
    }else {
        this.onlineStatus.setText("Online ");
        this.onlineStatus.setTextColor(getResources().getColor(R.color.colorBlue));
        this.onlineStatus.setBackgroundResource(R.drawable.blueround_stroke);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void postRequestSong (String UserName,String SongName) {

        retrofit= ApiResponse.retrofit(URL_REQUEST_SONG,this);

        jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<SuccessErrorModel> call = jsonApiHolder.postSongRequest("1",
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
                Toast.makeText(DjPrpfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void followUnFollow (String Url, int CurrentStatus) {
        Retrofit retrofit = ApiResponse.retrofit(Url,DjPrpfileActivity.this);
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);


        Call <SuccessErrorModel> call = jsonApiHolder.followUnFollowArtist(String.valueOf(artistID));

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
                Toast.makeText(DjPrpfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showLoadingDialogue() {

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialogue_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.show();

    }

}