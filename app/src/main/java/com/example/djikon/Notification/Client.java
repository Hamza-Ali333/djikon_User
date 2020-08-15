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

    public static Retrofit getClient (String BASEURL){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header( "Content-Type:", "application/json")
                                .header("Authorization:","key=AAAArtlB-KE:APA91bFVLEUan-vwsJjo_3U-Ajt32d--asVQV2RWg8b2Yz0OoMxjGLLozM94nA-XMkXQZtqRLEJvtfCXEzQH4DXHqTEx-yt-MIIElnKS6Arq4F5J1PkOZQ5jZAZHgxjHeuTqaxxoJBHE")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}
