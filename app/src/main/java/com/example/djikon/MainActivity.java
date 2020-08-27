package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.djikon.ApiHadlers.ApiClient;
import com.example.djikon.ApiHadlers.JSONApiHolder;
import com.example.djikon.GlobelClasses.App;
import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.NetworkChangeReceiver;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.Models.LoginRegistrationModel;
import com.example.djikon.Models.SuccessErrorModel;
import com.example.djikon.NavDrawerFragments.AllArtistFragment;
import com.example.djikon.NavDrawerFragments.ChatListFragment;
import com.example.djikon.NavDrawerFragments.SubscribedArtistFragment;
import com.example.djikon.NavDrawerFragments.LatestFeedFragment;
import com.example.djikon.NavDrawerFragments.LiveToArtistFragment;
import com.example.djikon.NavDrawerFragments.RequestedSongFragment;
import com.example.djikon.NavDrawerFragments.SocialMediaShareFragment;
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

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;

    private Toolbar toolbar;

    private static String IMAGE_URL ="http://ec2-54-161-107-128.compute-1.amazonaws.com/";
    private PreferenceData preferenceData;

    private CircularImageView currentUserProfile;
    private ImageView UserProfileHeader;
    private TextView UserName;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    String currentUserEmail;
    String currentUserPassword;
    Boolean isComeFromRegistrationActivity;

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

        //if User in not Register on FireBase then Register him
        try {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser == null) {
                Intent i = getIntent();
                isComeFromRegistrationActivity = i.getBooleanExtra("come_from_registration", false);
                currentUserEmail = i.getStringExtra("email");
                currentUserPassword = i.getStringExtra("password");
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
                        new LiveToArtistFragment()).commit();
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
                    PreferenceData.clearPrefrences(MainActivity.this);
                    mFirebaseAuth.getInstance().signOut();
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "please check Your Network", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginRegistrationModel> call, Throwable t) {
                Log.i("TAG", "onFailure: "+t.getMessage());
                alertDialog = DialogsUtils.showAlertDialog(MainActivity.this,false,"No Internet","Please Check Your Internet Connection");

            }
        });
    }

    private void getCurrentUserImage() {
        String imageName = PreferenceData.getUserImage(this);

        if (!imageName.equals("No Image") && !imageName.equals("no")){
            IMAGE_URL += imageName;
            Picasso.get().load(IMAGE_URL)
                    .placeholder(R.drawable.ic_doctor)
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
                    Log.i("TAG", "onComplete: SignIn Done");
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
                Call<SuccessErrorModel> call = jsonApiHolder.postFCMTokenForWeb(instanceIdResult.getToken());

                call.enqueue(new Callback<SuccessErrorModel>() {
                    @Override
                    public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                       if(!response.isSuccessful()){
                           //if failed to send token on server then run Again
                           sendFCMToken();
                       }
                    }
                    @Override
                    public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                        //if failed to send token on server then run Again
                        sendFCMToken();
                    }
                });

            }
        });
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}