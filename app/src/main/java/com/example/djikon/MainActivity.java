package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Toolbar toolbar;
    private PreferenceData preferenceData;
    String BASEURL_DATA="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
    String Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRefrencer();

        preferenceData = new PreferenceData();

        //Intent i = getIntent();
        //header = i.getStringExtra("Token");
        Token = preferenceData.getUserToken(MainActivity.this);

        Toast.makeText(this, Token, Toast.LENGTH_LONG).show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CircularImageView img = findViewById(R.id.loginUser);

        img.setOnClickListener(new View.OnClickListener() {
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

    private void createRefrencer(){
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);

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

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header( "Accept:", "application/json")
                                .header("Authorization","Bearer "+Token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

       // feedJsonApi.userlogout(preferenceData.getLoggedInUserToken(MainActivity.this));

        Call<SuccessToken> call = feedJsonApi.logout();

        call.enqueue(new Callback<SuccessToken>() {
            @Override
            public void onResponse(Call<SuccessToken> call, Response<SuccessToken> response) {
                if(response.isSuccessful()){
                    Log.i("TAG", "onResponse: "+response.code()+response.body().getSuccess());
                    PreferenceData preferenceData = new PreferenceData();
                    preferenceData.setUserLoggedInStatus(MainActivity.this,false);
                    preferenceData.clearLoggedInEmailAddress(MainActivity.this);
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
                else {
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessToken> call, Throwable t) {
                Log.i("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

}