package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogDetailActivity extends AppCompatActivity {


   private TextView txt_Title, txt_artist_Name, txt_Description,txt_CreateTime, txt_Total_Likes, txt_Total_Comments;
   private FrameLayout frameLayout;


    private SliderView sliderView;
    private ImageView img_Profile;

   private String BASEURL_IMAGES="http://ec2-54-161-107-128.compute-1.amazonaws.com/post_images/";
   private String BASEURL_DATA="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";

   private SingleBlog_Model singleBlog_model;
   private   List<SliderItem> sliderItems = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private   VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        getSupportActionBar().setTitle("Blog Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefreances();

        Intent intent = getIntent();
        String Url = "blog/";
        Url +=intent.getStringExtra("url");
        downloadBlogs(BASEURL_DATA,Url);


        //setting the controller's on the videoView
         MediaController mediaController = new MediaController(this);
         videoView.setMediaController(mediaController);


    }




    private void downloadBlogs(String SERVER_Url,String BlogId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);


        Call<SingleBlog_Model> call = feedJsonApi.getSingleBlog(BlogId);
        call.enqueue(new Callback<SingleBlog_Model>() {


            @Override
            public void onResponse(Call<SingleBlog_Model> call, Response<SingleBlog_Model> response) {
                if (response.isSuccessful()) {
                    String  Name = response.body().getArtist_name();
                    String  Gallery = response.body().getGallery();
                    String CreateTime = response.body().getCreated_at();
                    String Title = response.body().getTitle();
                    String Description = response.body().getDescription();
                    int Likes = response.body().getLikes_count();
                    int Comments = response.body().getComments_count();
                    String Video = response.body().getVideo();
                    String Profile = response.body().getArtist_profile_image();


                    //if comment is not Zero
                    mRecyclerView.setVisibility(View.GONE);
                    if(Comments != 0){
                        mRecyclerView.setVisibility(View.VISIBLE);
                        initializeCommentRecycler(response.body().comments);
                    }else {
                        Toast.makeText(BlogDetailActivity.this, "No Comments", Toast.LENGTH_SHORT).show();
                    }



                    //WillPass The Data Through this This Work Good
                   // singleBlog_model = new SingleBlog_Model(Title,CreateTime,Name,Gallery,Likes,Comments,Video,Description,Description);

                    setDataIntoFields(Name,Profile,Title,Description,Likes,Comments,CreateTime);

                    if (!Gallery.equals("no") || Gallery.isEmpty()) {

                        sliderView.setVisibility(View.VISIBLE);
                        Gallery =Gallery.replaceAll("\\[", "").replaceAll("\\]","").replace("\"", "");
                        String[] GalleryArray = Gallery.split(",");
                        initializeImageSlider(GalleryArray);
                    }else {
                        sliderView.setVisibility(View.GONE);
                    }

                    if (!Video.equals("no")) {
                        frameLayout.setVisibility(View.VISIBLE);
                        videoView.setVideoPath(Video);
                        videoView.start();
                    }else {
                        frameLayout.setVisibility(View.GONE);
                    }



                }else {
                    Toast.makeText(BlogDetailActivity.this,response.code(), Toast.LENGTH_SHORT).show();

                    return;
                }

            }
            @Override
            public void onFailure(Call<SingleBlog_Model> call, Throwable t) {
                Toast.makeText(BlogDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
               // progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void setDataIntoFields (String Name,
                                    String Profile ,
                                    String Title,
                                    String Description,
                                    int Likes,
                                    int Comments,
                                    String Date) {

        txt_Title.setText(Title);
        txt_artist_Name.setText(Name);
        txt_Description.setText(Description);

        txt_Total_Likes.setText(Integer.toString(Likes));
        txt_Total_Comments.setText(Integer.toString(Comments));
        txt_CreateTime.setText(Date);

        if (!Profile.equals("no")) {
            Picasso.get().load(Profile)
                    .placeholder(R.drawable.ic_doctor)
                    .into(img_Profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            //holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(BlogDetailActivity.this, "Something Happend Wrong Uploader Image", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    private void createRefreances(){
        txt_artist_Name= findViewById(R.id.txt_artist_name);
        txt_Title= findViewById(R.id.txt_title);
        txt_Description= findViewById(R.id.description);
        txt_Total_Comments = findViewById(R.id.txt_total_comment);
        txt_Total_Likes = findViewById(R.id.txt_total_like);
        txt_CreateTime = findViewById(R.id.date);
        sliderView = findViewById(R.id.imageSlider);
        videoView = findViewById(R.id.videoView);
        img_Profile = findViewById(R.id.img_profile);
        frameLayout = findViewById(R.id.fram);


        mRecyclerView = findViewById(R.id.chat_recycler);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initializeImageSlider(String[] Gallery){

        for(int i=0; i<=Gallery.length-1; i++){
            sliderItems.add(new SliderItem(BASEURL_IMAGES+Gallery[i]));
        }

        SliderAdapterExample adapter = new SliderAdapterExample(sliderItems,this);

        sliderView.setSliderAdapter(adapter);

        //tutorial should watch
        //sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }


    private void initializeCommentRecycler (List<Comment> commentList) {

        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(BlogDetailActivity.this);
        mAdapter = new RecyclerBlogComment(commentList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }



}