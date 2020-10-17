package com.ikonholdings.ikoniconnects.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.DjProfileActivity;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.ResponseModels.ChatModel;
import com.ikonholdings.ikoniconnects.ResponseModels.UserChatListModel;
import com.ikonholdings.ikoniconnects.Chat.Notification.APIService;
import com.ikonholdings.ikoniconnects.Chat.Notification.Client;
import com.ikonholdings.ikoniconnects.Chat.Notification.Data;
import com.ikonholdings.ikoniconnects.Chat.Notification.MyResponse;
import com.ikonholdings.ikoniconnects.Chat.Notification.Sender;
import com.ikonholdings.ikoniconnects.Chat.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircularImageView currentSubscriberProfile;
    private TextView toolBarTitle;

    private SwipeRefreshLayout pullToRefresh;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String chatNodeName;
    private DatabaseReference myRef;

    private Button btn_SendMsg;
    private EditText edt_Massage;

    private List<ChatModel> mChatModel;

    private ProgressDialog mProgressDialog;
    private Boolean alreadyHaveChat = false;

    private String subscriberId;
    private String subscriberName , imgProfileUrl;
    private String userName;
    private String CurrentUserId;

    private APIService apiService;
    private Boolean notify = false;

    private String Msg;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        userName = PreferenceData.getUserName(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_viewer);
        createReferences();
        setSupportActionBar(toolbar);
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        //give the Current Time and Date
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        //tool bar UserProfile
        currentSubscriberProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatViewerActivity.this, DjProfileActivity.class);
                i.putExtra("id",subscriberId);
                startActivity(i);
            }
        });

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getting data of the Receiver
        Intent i = getIntent();
        subscriberId =i.getStringExtra("subscriber_Id");
        subscriberName = i.getStringExtra("subscriber_Name");
        imgProfileUrl = i.getStringExtra("imgProfileUrl");

        setSubscriberProfile(imgProfileUrl);

        toolBarTitle.setText(subscriberName);//set Subscriber Name in tool bar

        myRef = FirebaseDatabase.getInstance().getReference("Chats");

        CurrentUserId = PreferenceData.getUserId(this);

        chatNodeName = "subscriberId_" + subscriberId + "_userId_" + CurrentUserId;
        checkHaveChatOrNot();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkHaveChatOrNot();
                pullToRefresh.setRefreshing(false);
            }
        });

        //this function will extract the only ids of user and dJ
//        String[] str = chatNodeName.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
//        Log.i("TAG", "onCreate: Subscriber "+str[1]);
//        Log.i("TAG", "onCreate: User "+str[3]);

        mProgressDialog = DialogsUtils.showProgressDialog(this,"Getting Massages","Please Wait");

        btn_SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String currentDateAndTime = sdf.format(new Date());
                if(!edt_Massage.getText().toString().isEmpty()){
                    if(!alreadyHaveChat){
                       //MAke Node for this new User if they are texting first time
                       // UserChatListModel userChatListModel = new UserChatListModel(String.valueOf(subscriberId), subscriberName, imgProfileUrl);
                       new CreateChatListOfUserAndSubscriber().execute();
                    }

                    Msg = edt_Massage.getText().toString();
                    sendMassage(edt_Massage.getText().toString(),
                            PreferenceData.getUserId(ChatViewerActivity.this), currentDateAndTime);
                }else{
                    Toast.makeText(ChatViewerActivity.this, "You Can't Send Empty massage", Toast.LENGTH_SHORT).show();
                }
                edt_Massage.setText("");
            }
        });

        edt_Massage.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!edt_Massage.getText().toString().isEmpty())
                    btn_SendMsg.setVisibility(View.VISIBLE);
                else
                    btn_SendMsg.setVisibility(View.GONE);

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                btn_SendMsg.setVisibility(View.GONE);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });

    }

    private void readMassages() {
        mChatModel = new ArrayList<>();

        myRef.child("Massages").child(chatNodeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mChatModel.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // snapshot object is every child of "Restaurant" that match the filter
                        //now here set data in to the field

                        mChatModel.add(new ChatModel(
                                snapshot.child("sender").getValue(String.class),
                                snapshot.child("message").getValue(String.class),
                                snapshot.child("time_stemp").getValue(String.class),
                                snapshot.getKey()
                                ));

                    }

                    mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time

                    mLayoutManager = new LinearLayoutManager(ChatViewerActivity.this);
                    mAdapter = new RecyclerChatViewer(mChatModel,
                            chatNodeName,
                            imgProfileUrl,
                            ChatViewerActivity.this
                            );

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    //scroll to end item of list
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                    scrollRecyclerToTheBottom();
                    mProgressDialog.dismiss();

                }else {
                    //open msg dailog
                    mProgressDialog.dismiss();
                    DialogsUtils.showAlertDialog(ChatViewerActivity.this,
                            false,
                            "No Data Found",
                            "Sorry You Not Have"
                    );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ChatViewerActivity.this, "not connected to the firebase", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setSubscriberProfile(String imageUrl){
        if (!imageUrl.equals("No Image") && !imageUrl.equals("no")){
            Picasso.get().load(ApiClient.Base_Url+imageUrl)
                    .placeholder(R.drawable.ic_avatar)
                    .fit()
                    .centerCrop()
                    .into(currentSubscriberProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

        }
    }

    private void checkHaveChatOrNot(){
        //check if User have already chat with this Subscriber
        myRef.child("Massages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Here check Node of this User Email and Subscriber Email is exit or not
                if (snapshot.hasChild(chatNodeName)) {

                    //when find node in db
                    //extract email of Subscriber on base of Current User Email
                    //String receiver = chatNodeName.replace("Hamza","");
                    //Toast.makeText(ChatViewerActivity.this, receiver, Toast.LENGTH_SHORT).show();

                    alreadyHaveChat = true;
                    readMassages();

                }
                else {

                    alreadyHaveChat = false;
                    mProgressDialog.dismiss();
                    Toast.makeText(ChatViewerActivity.this, "No Massages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatViewerActivity.this, "They Do not had any chat", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createReferences() {
        toolbar = findViewById(R.id.toolbar);
        toolBarTitle = findViewById(R.id.toolbar_title);
        currentSubscriberProfile = findViewById(R.id.currentUserProfile);

        pullToRefresh =findViewById(R.id.pullToRefresh);

        edt_Massage = findViewById(R.id.edt_sendmsg);
        btn_SendMsg = findViewById(R.id.send_msg);
        mRecyclerView = findViewById(R.id.chat_viewer_recycler);
    }

    private void sendMassage (String Massage, String Sender,String sendTime) {

        ChatModel chatModel = new ChatModel();
        chatModel.setSender(Sender);
        chatModel.setMessage(Massage);
        chatModel.setTime_stemp(sendTime);
        myRef.child("Massages").child(chatNodeName).push().setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //msg Send
                if(!alreadyHaveChat){
                    checkHaveChatOrNot();
                }
                alreadyHaveChat= true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //not send
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //nead to check this line what is the propose of this line
                // String user= dataSnapshot.getValue(String.class);
                if(notify){
                    sendNotification(subscriberId,
                            PreferenceData.getUserName(ChatViewerActivity.this),
                            Msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(String receiver,final String userName,final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(true,
                            PreferenceData.getUserId(ChatViewerActivity.this),
                            R.mipmap.ic_launcher,
                            userName+": "+message,"New Message",
                            subscriberId);

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(! response.isSuccessful()){
                                        //notification
                                       // Toast.makeText(ChatViewerActivity.this, "Failed to send Notification", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void scrollRecyclerToTheBottom () {
        //it will scroll recycler view to the bottom when keyboard open
        if (Build.VERSION.SDK_INT >= 11) {
            mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                mRecyclerView.smoothScrollToPosition(
                                        mRecyclerView.getAdapter().getItemCount());
                            }
                        }, 100);
                    }
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class CreateChatListOfUserAndSubscriber extends AsyncTask<Void,Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            //sender
            UserChatListModel userChatListModel = new UserChatListModel();
            userChatListModel.setsubscriber_Id(String.valueOf(subscriberId));
            userChatListModel.setsubscriber_Name(subscriberName);
            userChatListModel.setimgProfileUrl(imgProfileUrl);
           // userChatListModel.setStatus("online");
            myRef.child("chatListOfUser").child(CurrentUserId).push().setValue(userChatListModel);

            //receiver
            Map<String, String> userData = new HashMap<>();
            userData.put("user_Id", CurrentUserId);
            userData.put("user_Name",userName);
            userData.put("imgProfileUrl",imgProfileUrl);
           // userData.put("status","offline");
            myRef.child("chatListOfSubscriber").child(String.valueOf(subscriberId)).push().setValue(userData);

            return null;
        }
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
}