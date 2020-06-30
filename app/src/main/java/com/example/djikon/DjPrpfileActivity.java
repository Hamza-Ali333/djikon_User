package com.example.djikon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class DjPrpfileActivity extends AppCompatActivity {

    Button btn_Book_Artist, btn_Request_A_Song;
    RelativeLayout Service1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_profile);
        getSupportActionBar().setTitle("Julian Hudson");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

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

        Service1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DjPrpfileActivity.this, ServiceDetailActivity.class);
                startActivity(i);
            }
        });

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


        final AlertDialog alertDialog =  builder.show();

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

    }


    private void createRefrences () {

        btn_Book_Artist = findViewById(R.id.btn_book_artist);
        btn_Request_A_Song = findViewById(R.id.btn_RequestASong);
        Service1 = findViewById(R.id.rlt_servic1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}