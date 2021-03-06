package com.example.djikon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button btn_Proceed_To_Pay;

    private TextView txt_Service_Name,
            txt_Dj_Name,
            txt_Price,
            txt_Price_Type,
            txt_Description;

    private RatingBar ratingBar;

    private ImageView ServiceImage;


    private String serviceName,
    dj_Name,
    price,
    price_type,
    description,
    Gallery;

    private static final String BASEURL_IMAGES="http://ec2-54-161-107-128.compute-1.amazonaws.com/post_images/";

    private List<SliderItem> singleServiceModleArrayList;

    private static final String BASE_URL ="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/products/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().setTitle("Service Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createRefrences();

        Intent intent = getIntent();
        int id= intent.getIntExtra("id",0);

        downloadServiceData(String.valueOf(id));

        singleServiceModleArrayList = new ArrayList<>();




        btn_Proceed_To_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ServiceDetailActivity.this, BookArtistActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void downloadServiceData(String id){

        Retrofit retrofit = ApiResponse.retrofit(BASE_URL,this);

        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<SingleServiceModle> call = jsonApiHolder.getSingleServieData(id);

        call.enqueue(new Callback<SingleServiceModle>() {
            @Override
            public void onResponse(Call<SingleServiceModle> call, Response<SingleServiceModle> response) {

                if(response.isSuccessful()){
                    serviceName = response.body().getName();
                    dj_Name = response.body().getArtist_name();
                    price =String.valueOf(response.body().getPrice());
                    price_type = response.body().getPrice_type();
                    description = response.body().getDescription();



                    Gallery = response.body().getGallery();

                    setDataintoView();

                    if (!Gallery.equals("no") || Gallery.isEmpty()) {

                        mRecyclerView.setVisibility(View.VISIBLE);
                        Gallery =Gallery.replaceAll("\\[", "").replaceAll("\\]","").replace("\"", "");
                        String[] GalleryArray = Gallery.split(",");
                        buildServiceGalleryRecycler(GalleryArray);
                    }else {
                        mRecyclerView.setVisibility(View.GONE);
                    }



                }else {
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SingleServiceModle> call, Throwable t) {
                Toast.makeText(ServiceDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setDataintoView () {
        txt_Service_Name.setText(serviceName);
        txt_Dj_Name.setText(dj_Name);
        txt_Price.setText(" $"+price+" ");
        txt_Price_Type.setText(price_type);
        txt_Description.setText(description);

    }


    private void buildServiceGalleryRecycler (String[] gallery) {

        for(int i=0; i<=gallery.length-1; i++){
            singleServiceModleArrayList.add(new SliderItem(BASEURL_IMAGES+gallery[i]));
        }
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mAdapter = new RecyclerServiceGallery(singleServiceModleArrayList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createRefrences () {

        ServiceImage = findViewById(R.id.img_seervice_image);
        txt_Service_Name= findViewById(R.id.txt_Servic_Name);
        txt_Dj_Name = findViewById(R.id.txt_dj_name);
        txt_Price = findViewById(R.id.txt_service_charges);
        txt_Price_Type = findViewById(R.id.txt_price_type);
        txt_Description = findViewById(R.id.txt_service_discription);
        ratingBar = findViewById(R.id.ratingBar);

        mRecyclerView = findViewById(R.id.recyclerview_service_gallery);
        btn_Proceed_To_Pay = findViewById(R.id.btn_proceed_to_pay);

    }



}