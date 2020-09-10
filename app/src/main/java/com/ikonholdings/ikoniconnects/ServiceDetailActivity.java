package com.ikonholdings.ikoniconnects;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.ResponseModels.SingleServiceReviews;
import com.ikonholdings.ikoniconnects.ResponseModels.SingleServiceModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SliderModel;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerServiceGallery;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerServiceReviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceDetailActivity extends AppCompatActivity {

    private static final String TAG = "ServiceDetailActivity";
    //Service Recycler View
    private RecyclerView mGalleryRecyclerView;
    private RecyclerView.Adapter mGalleryAdapter;
    private RecyclerView.LayoutManager mGalleryLayoutManager;

    //Reviews Recycler View
    private RecyclerView mReviewsRecyclerView;
    private RecyclerView.Adapter mReviewsAdapter;
    private RecyclerView.LayoutManager mReviewsLayoutManager;
    
    private Button btn_Proceed_To_Pay;

    private TextView txt_Service_Name,
            txt_Dj_Name,
            txt_Price,
            txt_Price_Type,
            txt_Description,
    txt_ReviewHeading;

    private RatingBar ratingBar;

    private ImageView Featured_img;

    private ProgressBar progressBarProfile;

    private String
            serviceImage,
            serviceName,
            dj_Name,
            price,
            price_type,
            description,
            Gallery;
    private int serviceId;
    private float totalRating;

    private int artistId;
    private Bitmap BitmapImage;

    private static final String BASEURL_IMAGES = "http://ec2-52-91-44-156.compute-1.amazonaws.com/post_images/";
    private static final String FEATURED_IMAGES = "http://ec2-52-91-44-156.compute-1.amazonaws.com/";

    private List<SliderModel> singleServiceModleArrayList;
    private List<SingleServiceModel> singleServiceModels;
    private List<SingleServiceReviews> reviewsModels;


    private NetworkChangeReceiver mNetworkChangeReceiver;
    private AlertDialog alertDialog;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().setTitle("Service Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createReferences();
        retrofit = ApiClient.retrofit( this);
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        Intent intent = getIntent();
        serviceId = intent.getIntExtra("serviceId", 0);
        artistId = intent.getIntExtra("artistId", 0);
        BitmapImage = (Bitmap) intent.getParcelableExtra("BitmapImage");


        new GetServiceDataAndReviews().execute(String.valueOf(serviceId));

        singleServiceModleArrayList = new ArrayList<>();

        btn_Proceed_To_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addExtraToIntentAndLunchActivity(
                        Featured_img,
                        artistId,
                        false,
                        true,
                        serviceId,
                        price_type, //if find rate per hour then it will true or if not then it will be false
                        price,
                        serviceName,
                        description
                );
            }
        });
    }

    private void addExtraToIntentAndLunchActivity (ImageView img,
                                                   int artistId,
                                                   Boolean bookingForArtist,
                                                   Boolean bookingForService,
                                                   int serviceId,
                                                   String priceType,
                                                   String price,
                                                   String serviceNameOrDjName,
                                                   String description) {

        img.buildDrawingCache();
        Bitmap bitmap = img.getDrawingCache();

        Intent i = new Intent(ServiceDetailActivity.this, BookArtistOrServiceActivity.class);
        i.putExtra("artistId",artistId);
        i.putExtra("bookingForArtist", bookingForArtist);//booking Artist true
        i.putExtra("bookingForService", bookingForService);//booking Service true
        i.putExtra("serviceId",serviceId);
        i.putExtra("priceType", priceType);
        i.putExtra("BitmapImage", bitmap);
        i.putExtra("price", price);//rate per hour
        i.putExtra("serviceOrDjName", serviceNameOrDjName);//Name of Artist if booking him or Name of Service if Booking Artist Service
        i.putExtra("description", description);
        startActivity(i);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setDataInToView() {
        ratingBar.setRating(totalRating);
        txt_Service_Name.setText(serviceName);
        txt_Dj_Name.setText(dj_Name);
        txt_Price.setText(" $" + price + " ");
        txt_Price_Type.setText(price_type);
        txt_Description.setText(description);

        if(serviceImage != null){
            Picasso.get().load(FEATURED_IMAGES + serviceImage)
                    .fit()
                    .centerCrop()
                    .into(Featured_img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBarProfile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBarProfile.setVisibility(View.GONE);
                            Toast.makeText(ServiceDetailActivity.this, "Something Happend Wrong feed image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }//if service is not equal to null
    }


    private void buildServiceGalleryRecycler(String[] gallery) {

        for (int i = 0; i <= gallery.length - 1; i++) {
            singleServiceModleArrayList.add(new SliderModel(BASEURL_IMAGES + gallery[i]));
        }
        mGalleryRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mGalleryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mGalleryAdapter = new RecyclerServiceGallery(singleServiceModleArrayList);


        mGalleryRecyclerView.setLayoutManager(mGalleryLayoutManager);
        mGalleryRecyclerView.setAdapter(mGalleryAdapter);
    }

    private void buildReviewsRecyclerView(List<SingleServiceReviews> reviewsModels) {
        mReviewsRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mReviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsAdapter = new RecyclerServiceReviews(reviewsModels);

        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    }

    private void createReferences() {
        Featured_img = findViewById(R.id.img_seervice_image);
        txt_Service_Name = findViewById(R.id.txt_Servic_Name);
        txt_Dj_Name = findViewById(R.id.txt_dj_name);
        txt_Price = findViewById(R.id.txt_service_charges);
        txt_Price_Type = findViewById(R.id.txt_price_type);
        txt_Description = findViewById(R.id.txt_service_discription);
        txt_ReviewHeading = findViewById(R.id.reviewheading);
        ratingBar = findViewById(R.id.ratingBar);
        progressBarProfile = findViewById(R.id.progressBarProfile);

        mGalleryRecyclerView = findViewById(R.id.recyclerview_service_gallery);
        mReviewsRecyclerView = findViewById(R.id.reviews_recycler);
        btn_Proceed_To_Pay = findViewById(R.id.btn_proceed_to_pay);

    }

    private class GetServiceDataAndReviews extends AsyncTask<String,Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            singleServiceModels = new ArrayList<>();
            reviewsModels = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(String... strings) {

            jsonApiHolder = retrofit.create(JSONApiHolder.class);
            Call<SingleServiceModel> call = jsonApiHolder.getSingleServiceData("products/"+strings[0]);

            call.enqueue(new Callback<SingleServiceModel>() {
                @Override
                public void onResponse(Call<SingleServiceModel> call, Response<SingleServiceModel> response) {

                    if (response.isSuccessful()) {
                       // singleServiceModels = (List<SingleServiceModel>) response.body();
                        serviceId = response.body().getId();
                        serviceImage = response.body().getFeature_image();
                        serviceName = response.body().getName();
                        dj_Name = response.body().getArtist_name();
                        price = String.valueOf(response.body().getPrice());
                        price_type = response.body().getPrice_type();
                        totalRating = response.body().getRating();
                        description = response.body().getDescription();

                        btn_Proceed_To_Pay.setText("Proceed To Pay " + price + "$");
                        Gallery = response.body().getGallery();

                        reviewsModels = response.body().getSingleServiceReviews();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setDataInToView();

                                if (!Gallery.equals("no") || Gallery.isEmpty()) {

                                    mGalleryRecyclerView.setVisibility(View.VISIBLE);
                                    Gallery = Gallery.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
                                    String[] GalleryArray = Gallery.split(",");
                                    buildServiceGalleryRecycler(GalleryArray);

                                } else {
                                    mGalleryRecyclerView.setVisibility(View.GONE);
                                }

                                //Reviews RecyclerView
                                if (!reviewsModels.isEmpty()) {
                                    buildReviewsRecyclerView(reviewsModels);
                                }else {
                                    txt_ReviewHeading.setText("No Review Found");
                                    mReviewsRecyclerView.setVisibility(View.GONE);
                                }

                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<SingleServiceModel> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog = DialogsUtils.showResponseMsg(ServiceDetailActivity.this,true);
                        }
                    });
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }

}