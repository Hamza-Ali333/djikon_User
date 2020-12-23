package com.Ikonholdings.ikoniconnects.ApiHadlers;

import android.content.Context;

import com.Ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String Base_Url = "https://ikoniconnext.com/";

    public static Retrofit retrofit (Context context){
        PreferenceData preferenceData = new PreferenceData();
        String Token = preferenceData.getUserToken(context);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header( "Accept:", "application/json")
                                .header("Authorization","Bearer "+Token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_Url+"api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

}
