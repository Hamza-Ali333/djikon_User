package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
            txt_Progress_Msg;

    private ScrollView parenLayout;

    private CircularImageView img_DJ_Profile;


    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;


    private String mDJName,
            mAddress,
            mProfile,
            mAbout;

    private int mFollower_Count, mFollow_Status;

    private RecyclerView mServicesRecycler, mBlogRecyclerView;
    private RecyclerView.Adapter serviceAdapter;
    private RecyclerView.LayoutManager serviceLayoutManager;

    private RecyclerView.Adapter BlogAdapter;
    private RecyclerView.LayoutManager BlogLayoutManager;


    List<Services_Model> services;
    List<Dj_Blogs_Model> blogs;



    private final static String BASE_URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_profile);
        getSupportActionBar().setTitle("Dj Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        parenLayout.setVisibility(View.GONE);//hide the parent view

        showLoadingDialogue();//show lodaing dailoge while data is dowloading from the server

        services = new ArrayList<Services_Model>();
        Intent i = getIntent();
        int blogId = i.getIntExtra("id", 0);


        getProfileDataFromServer(String.valueOf(blogId));



        btn_Book_Artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DjPrpfileActivity.this, BookArtistActivity.class);
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


        if (mAddress.equals(null))
            Toast.makeText(this, "Address null", Toast.LENGTH_SHORT).show();
        else
            txt_address.setText(mAddress);


        txt_Total_Follower.setText("0");
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
                alertDialog.dismiss();

            }
        });

    }

    private void getProfileDataFromServer(String blogId) {

        Retrofit retrofit= ApiResponse.retrofit(BASE_URL,this);


        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<DJProfileModel> call = jsonApiHolder.getDjProfile(blogId);

        call.enqueue(new Callback<DJProfileModel>() {
            @Override
            public void onResponse(Call<DJProfileModel> call, Response<DJProfileModel> response) {

                if (response.isSuccessful()) {

                    Log.i("TAG", "onResponse: " + response.code());

                    mDJName = response.body().getFirstname() + " " + response.body().getLastname();
                    mAddress = response.body().getAddress();
                    mProfile = response.body().getProfile_image();
                    mFollower_Count = response.body().getFollowers();
                    mFollow_Status = response.body().getFollow_status();

                    services = response.body().getServices();
                    blogs = response.body().getBlog();

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

                } else {
                    Log.i("TAG", "onResponse: " + response.code());

                    return;
                }
            }

            @Override
            public void onFailure(Call<DJProfileModel> call, Throwable t) {

                Log.i("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(DjPrpfileActivity.this, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void createRefrences() {

        parenLayout = findViewById(R.id.scrollable);
        btn_Book_Artist = findViewById(R.id.btn_book_artist);
        btn_Request_A_Song = findViewById(R.id.btn_RequestASong);
        btn_Follow = findViewById(R.id.Follow_Dj);
        btn_Message = findViewById(R.id.message_dj);


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


    private void showLoadingDialogue() {

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialogue_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.show();

    }

}