package com.Ikonholdings.ikoniconnects.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.Ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.Ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.Ikonholdings.ikoniconnects.Chat.Fragment.MessageRoomFragment;
import com.Ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.Ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.Ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.AllArtistFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.BookingHistoryFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.CurrentLiveArtistFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.LatestFeedFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.LiveToArtistFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.RequestedSongFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.SocialMediaShareFragment;
import com.Ikonholdings.ikoniconnects.NavDrawerFragments.SubscribedArtistFragment;
import com.Ikonholdings.ikoniconnects.R;
import com.Ikonholdings.ikoniconnects.ResponseModels.LoginRegistrationModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;

    private Toolbar toolbar;

    private CircularImageView currentUserProfile;
    private ImageView UserProfileHeader;
    private TextView UserName;

    private ProgressDialog progressDialog;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private String currentUserEmail;
    private String currentUserPassword;
    private Boolean isComeFromRegistrationActivity;


    private DatabaseReference myRef;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);


        PreferenceData.registerPref(this,this);

        retrofit = ApiClient.retrofit(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createReferences();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        UserName.setText(PreferenceData.getUserName(MainActivity.this));

        getCurrentUserImage();

        setSupportActionBar(toolbar);
        //tool bar image
        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, UserProfileActivity.class));
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

    private void createReferences(){

        toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        navHeaderView =  navigationView.getHeaderView(0);

        currentUserProfile = findViewById(R.id.currentUserProfile);
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
                        new MessageRoomFragment()).commit();
                break;

            case R.id.nav_Following:
                getSupportActionBar().setTitle(R.string.SubscribedArtist);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SubscribedArtistFragment()).commit();
                break;

            case R.id.nav_SocialMedia:
                getSupportActionBar().setTitle(R.string.SocialMediaSharing);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SocialMediaShareFragment()).commit();
                break;

            case R.id.nav_RequestedSong:
                getSupportActionBar().setTitle(R.string.RequestedSong);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RequestedSongFragment()).commit();
                break;

            case R.id.nav_BookingHistory:
                getSupportActionBar().setTitle(R.string.BookingHistory);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BookingHistoryFragment()).commit();
                break;

            case R.id.nav_CurrentLiveArtist:
                getSupportActionBar().setTitle(R.string.CurrentLiveArtist);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CurrentLiveArtistFragment()).commit();
                break;

            case R.id.nav_Artist:
                getSupportActionBar().setTitle(R.string.Artist);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllArtistFragment()).commit();
                break;

            case R.id.nav_Logout:
                progressDialog= DialogsUtils.showProgressDialog(this,"Login Out","Please wait...");
                userLogOut();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void userLogOut () {
      jsonApiHolder = retrofit.create(JSONApiHolder.class);
      Call<LoginRegistrationModel> call = jsonApiHolder.logout();

        call.enqueue(new Callback<LoginRegistrationModel>() {
            @Override
            public void onResponse(Call<LoginRegistrationModel> call, Response<LoginRegistrationModel> response) {
                if(response.isSuccessful()){
                    PreferenceData.clearLoginState(MainActivity.this);
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Log.i("TAG", "onResponse: "+response.code());
                    DialogsUtils.showResponseMsg(MainActivity.this,
                            false);
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: "+t.getMessage());
                DialogsUtils.showResponseMsg(MainActivity.this,
                        true);
            }
        });
    }

    private void getCurrentUserImage() {
        String imageName = PreferenceData.getUserImage(this);

        if (!imageName.equals("No Image") && !imageName.equals("no")){
            Picasso.get().load(ApiClient.Base_Url + imageName)
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(currentUserProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //Navigation DrawerPhoto of User
                            UserProfileHeader.setImageDrawable(currentUserProfile.getDrawable());
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }

    }

    //send fcm Token to the Server for sending notification form server to application
    private void sendFCMToken() {
        //Get Firebase FCM token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                jsonApiHolder = retrofit.create(JSONApiHolder.class);
                Call<Void> call = jsonApiHolder.postFCMTokenForWeb(instanceIdResult.getToken());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                       if(!response.isSuccessful()){
                           //if failed to send token on server then run Again
                            DialogsUtils.showResponseMsg(MainActivity.this,false);
                       }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //if failed to send token on server then run Again
                        //alertDialog = DialogsUtils.showResponseMsg(MainActivity.this,true);
                        DialogsUtils.showAlertDialog(MainActivity.this,false,"no",t.getMessage());
                    }
                });
            }
        });
    }

    //when name change will automatically
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            UserName.setText(PreferenceData.getUserName(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUserImage();
    }

    @Override
    protected void onStop() {
        super.onStop();
         try {
            unregisterReceiver(mNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PreferenceData.unRegisterPref(this, this);
    }
}