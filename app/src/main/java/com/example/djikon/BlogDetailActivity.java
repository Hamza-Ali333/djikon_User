package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogDetailActivity extends AppCompatActivity {


    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        String Url = intent.getStringExtra("url");
        Url += "/";
        downloadBlogs("http://ec2-54-161-107-128.compute-1.amazonaws.com/api/");
        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.rectangle2,"Image Yo Yo"));
        sliderItems.add(new SliderItem(R.drawable.photo2,"Image No No"));
        sliderItems.add(new SliderItem(R.drawable.rectangle,"Image Yes Yes"));
        sliderItems.add(new SliderItem(R.drawable.photo3,"Night Event Yo"));
        sliderItems.add(new SliderItem(R.drawable.rectangle2,"Dj Service Night Yo Yo"));
        sliderItems.add(new SliderItem(R.drawable.rectangle,"You Have to Join this"));



        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(sliderItems,this);

        sliderView.setSliderAdapter(adapter);

       // sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        //video will play through this
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

    }




    private void downloadBlogs(String SERVER_Url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LatestFeedJsonApi feedJsonApi = retrofit.create(LatestFeedJsonApi.class);


        Call<SingleBlog_Model> call = feedJsonApi.getSingleBlog();
        call.enqueue(new Callback<SingleBlog_Model>() {
            @Override
            public void onResponse(Call<SingleBlog_Model> call, Response<SingleBlog_Model> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(BlogDetailActivity.this, response.code(), Toast.LENGTH_SHORT).show();

                    return;
                }
                JsonObject post = new JsonObject().get(response.body().toString()).getAsJsonObject();
                String Name =response.body().getArtist_name();
                List<String> Gallery = response.body().getGallery();
                String CreateTime = response.body().getCreated_at();
                String Title = response.body().getTitle();
                String Discription = response.body().getDescription();

                textView.setText(post+ "\n"+Gallery+ "\n"+ CreateTime+ "\n"+ Title+ "\n"+ Discription +"\n");




            }
            @Override
            public void onFailure(Call<SingleBlog_Model> call, Throwable t) {
                Toast.makeText(BlogDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
               // progressBar.setVisibility(View.GONE);
            }
        });
    }



}