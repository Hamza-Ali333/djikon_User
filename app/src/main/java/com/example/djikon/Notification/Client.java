package com.example.djikon.Notification;

import android.content.Context;

import com.example.djikon.GlobelClasses.PreferenceData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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
