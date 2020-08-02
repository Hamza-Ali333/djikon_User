package com.example.djikon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
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


public class BlogDetailActivity extends AppCompatActivity {


   private TextView txt_Title, txt_artist_Name, txt_Description,txt_CreateTime, txt_Total_Likes, txt_Total_Comments;
   private Button btn_SendComment;
   private EditText edt_Comment;
   private FrameLayout frameLayout;
   private NestedScrollView parentLayout;
   private ProgressBar progressBar;
   private TextView loading;


    private SliderView sliderView;
    private ImageView img_Profile;

   private static final String BASEURL_IMAGES="http://ec2-54-161-107-128.compute-1.amazonaws.com/post_images/";
   private static final String BASEURL_DATA="http://ec2-54-161-107-128.compute-1.amazonaws.com/api/";
   private static final String ADD_COMMENT_URL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/comment_store/";

   private int   blogId;
   private String  Gallery;
   private String Video;


   private List<SliderItem> sliderItems = new ArrayList<>();
   private List <Comment> mCommentList;
   private  String Featured_image;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private   VideoView videoView;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private NetworkChangeReceiver mNetworkChangeReceiver;


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
        setContentView(R.layout.activity_blog_detail);
        getSupportActionBar().setTitle("Blog Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();
        parentLayout.setVisibility(View.GONE);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        Log.i("TAG", "threadm: "+Thread.currentThread().getId());

        showLoadingDialogue(); //show loading Dialogue when it's downloading from server

        mCommentList = new ArrayList<>();//for avoid null pointer exeption when there is no comment in data base


        Intent intent = getIntent();
        String Url = "blog/";

        int id =intent.getIntExtra("url", 0);
        Url += String.valueOf(id);
       Featured_image = intent.getStringExtra("featured_image");


        downloadBlogs(BASEURL_DATA,Url);


        //setting the controller's on the videoView
         MediaController mediaController = new MediaController(this);
         videoView.setMediaController(mediaController);


         btn_SendComment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!edt_Comment.getText().toString().trim().isEmpty()){
                     String comment = edt_Comment.getText().toString().trim();
                     edt_Comment.getText().clear();
                     hideKeyboard(BlogDetailActivity.this);

                     if ( !mCommentList.isEmpty() && mCommentList!=null ) {
                         mCommentList.add(0,new Comment(comment,"123go","go go go","Current User","no"));
                         mAdapter.notifyDataSetChanged();
                     }else {
                         mCommentList.add(0,new Comment(comment,"123go","go go go","Current User","no"));
                         mRecyclerView.setVisibility(View.VISIBLE);
                         initializeCommentRecycler(mCommentList);
                     }


                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             postComment(comment);
                         }
                     },1000);

                 }else {
                     Toast.makeText(BlogDetailActivity.this, "Please Right Some Comment First", Toast.LENGTH_SHORT).show();
                 }
             }
         });





        edt_Comment.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!edt_Comment.getText().toString().isEmpty())
                btn_SendComment.setVisibility(View.VISIBLE);
                else
                    btn_SendComment.setVisibility(View.GONE);

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                btn_SendComment.setVisibility(View.GONE);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });



    }




    private void downloadBlogs(String SERVER_Url,String BlogId) {

        Retrofit retrofit= ApiResponse.retrofit(SERVER_Url,this);
        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);


        Call<SingleBlog_Model> call = feedJsonApi.getSingleBlog(BlogId);
        call.enqueue(new Callback<SingleBlog_Model>() {


            @Override
            public void onResponse(Call<SingleBlog_Model> call, Response<SingleBlog_Model> response) {
                if (response.isSuccessful()) {
                    String  Name = response.body().getArtist_name();
                    Gallery = response.body().getGallery();
                    String CreateTime = response.body().getCreated_at();
                    String Title = response.body().getTitle();
                    String Description = response.body().getDescription();
                    int Likes = response.body().getLikes_count();
                    int Comments = response.body().getComments_count();
                    Video = response.body().getVideo();
                    String Profile = response.body().getArtist_profile_image();
                    blogId = response.body().getId();


                    alertDialog.dismiss();
                    parentLayout.setVisibility(View.VISIBLE);

                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run(){

                            //if comment is not Zero build recycler view
                            mRecyclerView.setVisibility(View.GONE);
                            if(Comments != 0){
                                mRecyclerView.setVisibility(View.VISIBLE);
                                initializeCommentRecycler(response.body().comments);
                            }
                        }
                    });
                    thread.start();

                    setDataIntoFields(Name,Profile,Title,Description,Likes,Comments,CreateTime);

                    //WillPass The Data Through this This Work Good
                   // singleBlog_model = new SingleBlog_Model(Title,CreateTime,Name,Gallery,Likes,Comments,Video,Description,Description);

                            if (!Gallery.equals("no") || Gallery.isEmpty()) {

                                sliderView.setVisibility(View.VISIBLE);
                                Gallery =Gallery.replaceAll("\\[", "").replaceAll("\\]","").replace("\"", "");
                                String[] GalleryArray = Gallery.split(",");
                                initializeImageSlider(GalleryArray,true);
                            }else {
                                initializeImageSlider(null,false);
                            }



                    Thread VideoThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("TAG", "thread2: "+Thread.currentThread().getId());
                            if (!Video.equals("no")) {
                                frameLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.VISIBLE);
                                videoView.setVideoPath(Video);
                                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                                    public void onPrepared(MediaPlayer arg0) {
                                        progressBar.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);
                                        videoView.start();
                                    }
                                });

                            }else {
                                frameLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                    VideoThread.start();


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

    private void postComment(String Comment){

        Retrofit retrofit = ApiResponse.retrofit(ADD_COMMENT_URL,this);

        JSONApiHolder feedJsonApi = retrofit.create(JSONApiHolder.class);

        Call<SuccessErrorModel> call = feedJsonApi.postComment(String.valueOf(blogId),Comment);

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){

                }else {
                    Log.i("TAG", "onResponse: "+response.code()+"\n"+response.errorBody()+"\n"+response.body());
                    Toast.makeText(BlogDetailActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                Toast.makeText(BlogDetailActivity.this, "Network Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
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
        txt_artist_Name.setText("By: "+Name);
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

    private void createRefrences(){

        parentLayout = findViewById(R.id.parent);
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

        progressBar = findViewById(R.id.progress_circular);
        loading = findViewById(R.id.loading);

        btn_SendComment= findViewById(R.id.btn_sendcomment);
        edt_Comment = findViewById(R.id.edt_comment);

        mRecyclerView = findViewById(R.id.chat_recycler);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initializeImageSlider(String[] Gallery,Boolean Slider){

        if(Slider){
            for(int i=0; i<=Gallery.length-1; i++){
                sliderItems.add(new SliderItem(BASEURL_IMAGES+Gallery[i]));
            }
        }else {
            sliderItems.add(new SliderItem(Featured_image));
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

        mCommentList = commentList;
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(BlogDetailActivity.this);
        mAdapter = new RecyclerBlogComment(mCommentList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showLoadingDialogue() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialogue_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog =  builder.show();
    }


}