package com.ikonholdings.ikoniconnects.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    public static Retrofit getClient (String Url){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
