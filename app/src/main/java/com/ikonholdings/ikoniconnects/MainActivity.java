package com.ikonholdings.ikoniconnects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.CurrentLiveArtistFragment;
import com.ikonholdings.ikoniconnects.ResponseModels.LoginRegistrationModel;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.AllArtistFragment;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.ChatListFragment;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.SubscribedArtistFragment;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.LatestFeedFragment;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.LiveToArtistFragment;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.RequestedSongFragment;
import com.ikonholdings.ikoniconnects.NavDrawerFragments.SocialMediaShareFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;

    private Toolbar toolbar;

    private static String IMAGE_URL ="http://ec2-52-91-44-156.compute-1.amazonaws.com/";
    private PreferenceData preferenceData;

    private CircularImageView currentUserProfile;
    private ImageView UserProfileHeader;
    private TextView UserName;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private String currentUserEmail;
    private String currentUserPassword;
    private Boolean isComeFromRegistrationActivity;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference myRef;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
        mFirebaseAuth = FirebaseAuth.getInstance();

        PreferenceData.registerPref(this,this);

        //if User in not Register on FireBase then Register him
        try {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser == null) {
                Intent i = getIntent();
                isComeFromRegistrationActivity = i.getBooleanExtra("come_from_registration", false);
                currentUserEmail = PreferenceData.getUserEmail(this);
                currentUserPassword = PreferenceData.getUserPassword(this);
                if (currentUserEmail != null && currentUserEmail != null)
                    new RegisteringUserAlsoOnFirebase().execute(isComeFromRegistrationActivity);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        retrofit = ApiClient.retrofit(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createReferences();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        preferenceData = new PreferenceData();

        UserName.setText(PreferenceData.getUserName(MainActivity.this));

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

    private void createReferences(){

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
                        new ChatListFragment()).commit();
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

                progressDialog= DialogsUtils.showProgressDialog(this,"LogingOut","Please wait...");
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
                    mFirebaseAuth.getInstance().signOut();
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Log.i("TAG", "onResponse: "+response.code());
                    alertDialog = DialogsUtils.showResponseMsg(MainActivity.this,
                            false);
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: "+t.getMessage());
                alertDialog = DialogsUtils.showResponseMsg(MainActivity.this,
                        true);
            }
        });
    }

    private void getCurrentUserImage() {
        String imageName = PreferenceData.getUserImage(this);

        if (!imageName.equals("No Image") && !imageName.equals("no")){
            Picasso.get().load(IMAGE_URL + imageName)
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

    private void creatingUserOnFirebase(String Email, String Password) {
        //Creating DJ also on Firebase for Chat System
        mFirebaseAuth.createUserWithEmailAndPassword
                (Email, Password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserIDAndUIDOnFirebase();
                            sendFCMToken();
                        }
                    }
                });
    }

    private void signInUserOnFirebase(String Email, String Password) {
        //SignIn DJ Also on FireBase For ChatSystem
        mFirebaseAuth.signInWithEmailAndPassword(Email,
                Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //if user is not exit in data base but successfully Sign in ON server then should create also on firebase
                    creatingUserOnFirebase(Email, Password);

                }else {
                    saveUserIDAndUIDOnFirebase();
                    sendFCMToken();
                }
            }
        });
    }

    private void saveUserIDAndUIDOnFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("All_Users");

        if (mFirebaseUser != null) {
            String userUId = mFirebaseUser.getUid();
            Map<String, String> userData = new HashMap<>();
            userData.put("uid", userUId);
            userData.put("server_id", preferenceData.getUserId(this));

            myRef.child("Users").child(preferenceData.getUserId(this)).setValue(userData);
        } else {
            Log.i("TAG", "saveUserIDAndUIDonFirebase: no user found");
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
                           alertDialog = DialogsUtils.showResponseMsg(MainActivity.this,false);
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

    private class RegisteringUserAlsoOnFirebase extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected Void doInBackground(Boolean... booleans) {
            if (booleans[0]) {
                //isUserComeFromRegistrationActivity
                creatingUserOnFirebase(currentUserEmail, currentUserPassword);
            } else {
                //isUserComeFromSignIn
                signInUserOnFirebase(currentUserEmail, currentUserPassword);
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUserImage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
        PreferenceData.unRegisterPref(this, this);
    }
}