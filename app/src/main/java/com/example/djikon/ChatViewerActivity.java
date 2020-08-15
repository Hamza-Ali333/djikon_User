package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djikon.GlobelClasses.DialogsUtils;
import com.example.djikon.GlobelClasses.PreferenceData;
import com.example.djikon.Models.ChatModel;
import com.example.djikon.Models.UserChatListModel;
import com.example.djikon.Notification.APIService;
import com.example.djikon.Notification.Client;
import com.example.djikon.Notification.Data;
import com.example.djikon.Notification.MyResponse;
import com.example.djikon.Notification.Sender;
import com.example.djikon.Notification.Token;
import com.example.djikon.RecyclerView.RecyclerChatViewer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ChatViewerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircularImageView currentUserProfile;
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
    private AlertDialog alertDialog;
    private Boolean alreadyHaveChat = false;

    int djId;
    String djName , imgProfileUrl;
    String userId;

    private APIService apiService;
    private FirebaseUser fuser;
    private Boolean notify = false;

    @Override
    protected void onStart() {
        super.onStart();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_viewer);
        createRefrences();
        setSupportActionBar(toolbar);

        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        //give the Current Time and Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        //tool bar UserProfile
        currentUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatViewerActivity.this, DjProfileActivity.class);
                i.putExtra("id",djId);
                startActivity(i);
            }
        });


        myRef = FirebaseDatabase.getInstance().getReference("Chats");


        //getting email of the Receiver
        Intent i = getIntent();
        djId =i.getIntExtra("id",0);
        djName = i.getStringExtra("djName");
        imgProfileUrl = i.getStringExtra("imgProfileUrl");
        toolBarTitle.setText(djName);//set DJ Name in tool bar
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setDjProfile(imgProfileUrl);

        userId = PreferenceData.getUserId(this);


        chatNodeName = "djId_"+ djId +"_userId_"+userId;
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
//        Log.i("TAG", "onCreate: Dj "+str[1]);
//        Log.i("TAG", "onCreate: User "+str[3]);

        mProgressDialog = DialogsUtils.showProgressDialog(this,"Getting Massages","Please Wait");

        btn_SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String currentDateandTime = sdf.format(new Date());
                if(!edt_Massage.getText().toString().isEmpty()){
                    if(!alreadyHaveChat){
                        //MAke Node for this new User if they are texting first time
                       // UserChatListModel userChatListModel = new UserChatListModel(String.valueOf(djId), djName, imgProfileUrl);
                        UserChatListModel userChatListModel = new UserChatListModel();
                        userChatListModel.setId(String.valueOf(djId));
                        userChatListModel.setDj_Name(djName);
                        userChatListModel.setImageUrl(imgProfileUrl);
                        myRef.child("chatListOfUser").child(userId).push().setValue(userChatListModel);
                    }

                    sendMassage(edt_Massage.getText().toString(),userId,String.valueOf(djId),currentDateandTime);
                }else{
                    Toast.makeText(ChatViewerActivity.this, "You Can't Send Empty massage", Toast.LENGTH_SHORT).show();
                }
                edt_Massage.setText("");
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
                                snapshot.child("receiver").getValue(String.class),
                                snapshot.child("message").getValue(String.class),
                                snapshot.child("time_stemp").getValue(String.class),
                                snapshot.getKey()
                                ));

                    }
                    mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time

                    mLayoutManager = new LinearLayoutManager(ChatViewerActivity.this);
                    mAdapter = new RecyclerChatViewer(mChatModel,userId,chatNodeName);


                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    //scroll to end item of list
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                    scrollRecyclerToTheBottom();
                    mProgressDialog.dismiss();

                }else {
                    //open msg dailog
                    mProgressDialog.dismiss();
                    alertDialog = DialogsUtils.showAlertDialog(ChatViewerActivity.this,
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

    private void setDjProfile(String imageUrl){
        if (!imageUrl.equals("No Image") && !imageUrl.equals("no")){

            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.ic_doctor)
                    .fit()
                    .centerCrop()
                    .into(currentUserProfile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {
                            // Toast.makeText(getC, "Something Happend Wrong feed image", Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    private void checkHaveChatOrNot(){
        //check if User have already chat with this DJ
        myRef.child("Massages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Here check Node of this User Email and DJ Email is exit or not
                if (snapshot.hasChild(chatNodeName)) {

                    //when find node in db
                    //extract email of DJ on base of Current User Email
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


    private void createRefrences() {

        toolbar = findViewById(R.id.toolbar);
        toolBarTitle = findViewById(R.id.toolbar_title);
        currentUserProfile = findViewById(R.id.currentUserProfile);

        pullToRefresh =findViewById(R.id.pullToRefresh);

        edt_Massage = findViewById(R.id.edt_sendmsg);
        btn_SendMsg = findViewById(R.id.btn_send_msg);
        mRecyclerView = findViewById(R.id.chat_viewer_recycler);
    }

    private void sendMassage (String Massage, String Sender, String Receiver,String sendTime) {

        //ChatModel chatModel = new ChatModel(Sender, Receiver, Massage,sendTime);
        ChatModel chatModel = new ChatModel();
        chatModel.setSender(Sender);
        chatModel.setReceiver(Receiver);
        chatModel.setMessage(Massage);
        chatModel.setTime_stemp(sendTime);
        myRef.child("Massages").child(chatNodeName).push().setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChatViewerActivity.this, "Send", Toast.LENGTH_SHORT).show();
                if(!alreadyHaveChat){
                    checkHaveChatOrNot();
                }
                alreadyHaveChat= true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatViewerActivity.this, "Not Send", Toast.LENGTH_SHORT).show();
            }
        });

        final String msg = Massage;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //nead to check this line what is the propose of this line
                // String user= dataSnapshot.getValue(String.class);
                if(notify){
                    sendNotification(Receiver,"CurrentUserName",msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendNotification(String receiver,final String userName,final String messaage) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,userName+": "+messaage,"New Message",
                            userId);

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(! response.isSuccessful()){
                                        Toast.makeText(ChatViewerActivity.this, "Faild to send Notification", Toast.LENGTH_SHORT).show();
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

}