package com.example.djikon;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceData
{
    static final String PREF_LOGGEDIN_USER_TOKEN = "logged_token_is";
    static final String PREF_LOGGEDIN_USER_ID = "logged_id_is";
    static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    static final String PREF_USER_IAMAGE = "Current_User_Image";


    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserToken(Context ctx, String token)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_TOKEN, token);
        editor.apply();
        editor.commit();
    }

    public static String getUserToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_TOKEN, "No Token");
    }



    public static void setUserId(Context ctx, String id)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_ID, id);
        editor.apply();
        editor.commit();
    }

    public static String getUserId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_ID, "No Id");
    }


    public static void setUserImage(Context ctx, String imageName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_IAMAGE, imageName);
        editor.apply();
        editor.commit();
    }

    public static String getUserImage(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_IAMAGE, "No Image");
    }


    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    //clear the memory of the shared prefrences
    public static void clearPrefrences(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_TOKEN);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.commit();
    }
}