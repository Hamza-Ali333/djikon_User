package com.example.djikon;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class App extends Application {
    private static Resources sResources;

    //--I want to load strings resources from anywhere--
    public static String loadStringResource(int resID) {
        return sResources.getString(resID);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(getApplicationContext(), AppService.class);
        startService(intent);
    }
}
