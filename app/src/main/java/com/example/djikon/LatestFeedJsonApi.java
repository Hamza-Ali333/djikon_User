package com.example.djikon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LatestFeedJsonApi {

    @GET ("blog")
    Call<List<Blog_Model>> getBlogs();

    @GET("blog/1")
    Call<SingleBlog_Model> getSingleBlog();
}


