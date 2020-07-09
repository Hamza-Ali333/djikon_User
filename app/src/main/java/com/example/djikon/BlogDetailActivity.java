package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class BlogDetailActivity extends AppCompatActivity {


   private TextView txt_Title, txt_artist_Name, txt_Description,txt_CreateTime, txt_Total_Likes, txt_Total_Comments;

   TextView textView;
    SliderView sliderView;

   String BASEURL_IMAGES="http://ec2-54-161-107-128.compute-1.amazonaws.com/post_images/";
   String BASEURL_DATA="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";

    List<SliderItem> sliderItems = new ArrayList<>();

    VideoView videoView;
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










        //video will play through this
         videoView = findViewById(R.id.videoView);
//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
//        Uri uri = Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
       MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);



    }




    private void downloadBlogs(String SERVER_Url,String BlogId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LatestFeedJsonApi feedJsonApi = retrofit.create(LatestFeedJsonApi.class);


        Call<SingleBlog_Model> call = feedJsonApi.getSingleBlog(BlogId);
        call.enqueue(new Callback<SingleBlog_Model>() {


            @Override
            public void onResponse(Call<SingleBlog_Model> call, Response<SingleBlog_Model> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(BlogDetailActivity.this,response.code(), Toast.LENGTH_SHORT).show();

                    return;
                }else {

                    String  Name = response.body().getArtist_name();
                    String  Gallery = response.body().getGallery();
                    String CreateTime = response.body().getCreated_at();
                    String Title = response.body().getTitle();
                    String Description = response.body().getDescription();
                    String Likes = response.body().getLikes_count();
                    String Comments = response.body().getComments_count();
                    String Video = response.body().getVideo();

                    setDataIntoFields(Name,Title,Description,Likes,Comments,CreateTime);


                    Gallery =Gallery.replaceAll("\\[", "").replaceAll("\\]","").replace("\"", "");

                    String[] GalleryArray = Gallery.split(",");
                    int TotalImages =GalleryArray.length;
                    textView.setText(GalleryArray[0]+"\n"+GalleryArray[1]+"\nTotal Images:   "+TotalImages);


                    videoView.setVideoPath(Video);
                    videoView.start();



                    initializeImageSlider(GalleryArray);


                }

            }
            @Override
            public void onFailure(Call<SingleBlog_Model> call, Throwable t) {
                Toast.makeText(BlogDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
               // progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void setDataIntoFields(String Name, String Title, String Description, String Likes, String Comments,String Date){
        txt_Title.setText(Title);
        txt_artist_Name.setText(Name);
        txt_Description.setText(Description);
        txt_Total_Likes.setText(Likes);
        txt_Total_Comments.setText(Comments);
        txt_CreateTime.setText(Date);
    }

    private void createRefreances(){
        txt_artist_Name= findViewById(R.id.txt_artist_name);
        txt_Title= findViewById(R.id.txt_title);
        txt_Description= findViewById(R.id.description);
        txt_Total_Comments = findViewById(R.id.txt_total_comment);
        txt_Total_Likes = findViewById(R.id.txt_total_like);
        txt_CreateTime = findViewById(R.id.date);
        textView = findViewById(R.id.textView15);
        sliderView = findViewById(R.id.imageSlider);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initializeImageSlider(String[] Gallery){


        for(int i=0; i<=Gallery.length-1; i++){
            sliderItems.add(new SliderItem(BASEURL_IMAGES+Gallery[i],"Image no :"+i));
        }

        SliderAdapterExample adapter = new SliderAdapterExample(sliderItems,this);

        sliderView.setSliderAdapter(adapter);

        //sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }

}