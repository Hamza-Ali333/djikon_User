package com.example.djikon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface LatestFeedJsonApi {

    @GET ("blog")
    Call<List<Blog_Model>> getBlogs();

    @GET
    Call<SingleBlog_Model> getSingleBlog(@Url String url);


}


