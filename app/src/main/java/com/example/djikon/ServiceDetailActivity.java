package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServiceDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Button btn_Proceed_To_Pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().setTitle("Service Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();

        mRecyclerView = findViewById(R.id.recyclerview_service_gallery);

        ArrayList<ServiceImage_Model> serviceImage_modelArrayList = new ArrayList<>();

        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo2,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.dj_event,"Catering Service In Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo2,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));

        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.woman,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.dj_event,"Catering Service In Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo2,"Night Event"));
        serviceImage_modelArrayList.add(new ServiceImage_Model(R.drawable.photo3,"Music Night In Us"));

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        mAdapter = new RecyclerServiceGallery(serviceImage_modelArrayList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        btn_Proceed_To_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ServiceDetailActivity.this, BookArtistActivity.class);
                startActivity(i);
            }
        });

    }

    private void createRefrences () {
        btn_Proceed_To_Pay = findViewById(R.id.btn_proceed_to_pay);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}