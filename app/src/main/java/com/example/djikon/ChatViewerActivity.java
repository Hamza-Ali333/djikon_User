package com.example.djikon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewerActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private String ReciverEmail;

    private DatabaseReference myRef;

    private Button btn_SendMsg;
    private EditText edt_Massage;

    private List<ChatModel> mChatModel;

    private ProgressDialog mProgressDialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_viewer);
        createRefrences();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Chats");
        //getting email of the Receiver
        Intent i = getIntent();
        ReciverEmail =i.getStringExtra("email");
        ReciverEmail += "Hamza";

        mProgressDialog = DialogsUtils.showProgressDialog(this,"Getting Massages","Please Wait");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(ReciverEmail)) {

                    //here we will extract email of receiver
                    String receiver = ReciverEmail.replace("Hamza","");
                    Toast.makeText(ChatViewerActivity.this, receiver, Toast.LENGTH_SHORT).show();

                    readMassages();

                }
                else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ChatViewerActivity.this, "They Do't talk yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatViewerActivity.this, "They Do not had any chat", Toast.LENGTH_SHORT).show();
            }
        });

        btn_SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edt_Massage.getText().toString().isEmpty()){
                    sendMassage(edt_Massage.getText().toString(),"Hamza","Bilawal");
                }else{
                    Toast.makeText(ChatViewerActivity.this, "You Can't Send Empty massage", Toast.LENGTH_SHORT).show();
                }
                edt_Massage.setText("");
            }
        });

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

    private void readMassages() {
        mChatModel = new ArrayList<>();

        myRef.child(ReciverEmail).addValueEventListener(new ValueEventListener() {
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
                                snapshot.child("time").getValue(String.class)
                                ));

                        //for also getting the key of the node
                        //snapshot.getKey()
                    }

                    //mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time

                    mLayoutManager = new LinearLayoutManager(ChatViewerActivity.this);
                    mAdapter = new RecyclerChatViewer(mChatModel,"Hamza");


                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    //scroll to end item of list
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

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

    private void createRefrences() {
        edt_Massage = findViewById(R.id.edt_sendmsg);
        btn_SendMsg = findViewById(R.id.btn_send_msg);
        mRecyclerView = findViewById(R.id.chat_viewer_recycler);
    }

    private void sendMassage (String Massage, String Sender, String Receiver) {

        ChatModel chatModel = new ChatModel(Sender, Receiver, Massage,"10:10");

        myRef.child(ReciverEmail).push().setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChatViewerActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatViewerActivity.this, "Do it again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}