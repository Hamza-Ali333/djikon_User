package com.example.djikon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.jetbrains.annotations.Nullable;

public class AppService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
//        PreferenceData preferenceData= new PreferenceData();
//
//        preferenceData.clearPrefrences(this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
