package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;

    private Toolbar toolbar;
    private static final String BASEURL ="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
    private static String IMAGEURL ="http://ec2-54-161-107-128.compute-1.amazonaws.com/";
    private  PreferenceData preferenceData;

    private CircularImageView currentUserProfile;
    private ImageView UserProfileHeader;
    private TextView UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createRefrences();


        preferenceData = new PreferenceData();


        UserName.setText(preferenceData.getUserName(MainActivity.this));


        getCurrentUserImage();

        setSupportActionBar(toolbar);


//tool bar image
        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(i);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what do you want here

            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                  R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //if the activity luch for the first time and saveInstante null then it set the fragment
        if(savedInstanceState == null) {
            getSupportActionBar().setTitle(R.string.latestFeed);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new LatestFeedFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_LatestFeed);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  {
            super.onBackPressed();
        }
    }

    private void createRefrences(){

        toolbar = findViewById(R.id.toolbar);
        currentUserProfile = findViewById(R.id.currentUserProfile);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        navHeaderView =  navigationView.getHeaderView(0);

        UserProfileHeader = navHeaderView.findViewById(R.id.img_userProfile);
        UserName = navHeaderView.findViewById(R.id.txt_userName);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {




        switch (menuItem.getItemId()) {

            case R.id.nav_LatestFeed:

                getSupportActionBar().setTitle(R.string.latestFeed);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LatestFeedFragment()).commit();
            break;

            case R.id.nav_LiveToArtist:

                getSupportActionBar().setTitle(R.string.liveToArtist);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LiveToArtistFragment()).commit();
                break;

            case R.id.nav_ChatArea:

                getSupportActionBar().setTitle(R.string.ChatArea);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChatAreaFragment()).commit();
                break;

            case R.id.nav_SubscribedArtist:

                getSupportActionBar().setTitle(R.string.SubscribeArtist);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SubscribeArtistFragment()).commit();

                break;

            case R.id.nav_SocialMedia:

                getSupportActionBar().setTitle(R.string.SocialMediaSharing);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SocialMediaShareFragment()).commit();

                break;

            case R.id.nav_CurrentLiveArtist:

                getSupportActionBar().setTitle(R.string.CurrentLiveArtist);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LiveToArtistFragment()).commit();
                break;

            case R.id.nav_Logout:
                userLogOut();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private  void userLogOut () {

      Retrofit retrofit= ApiResponse.retrofit(BASEURL,this);

      JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);


        Call<LoginRegistrationModel> call = jsonApiHolder.logout();

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                if(response.isSuccessful()){
                    Log.i("TAG", "onResponse: "+response.code()+response.body().getSuccess());

                    preferenceData.clearPrefrences(MainActivity.this);

                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();

                }
                else {
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

    private void getCurrentUserImage() {

        String imageName = preferenceData.getUserImage(this);

        if (!imageName.equals("No Image") && !imageName.equals("no")){
            Toast.makeText(this, "findIamge", Toast.LENGTH_SHORT).show();
            IMAGEURL += imageName;
            Picasso.get().load(IMAGEURL)
                    .fit()
                    .centerCrop()
                    .into(currentUserProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            UserProfileHeader.setImageDrawable(currentUserProfile.getDrawable());
                            // holder.txt_Loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Toast.makeText(getC, "Something Happend Wrong feed image", Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }

}