package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceData
{
    static final String PREF_BUILD_VERSION = "build_version";
    static final String PREF_LOGGEDIN_USER_TOKEN = "logged_token_is";
    static final String PREF_LOGGEDIN_USER_ID = "logged_id_is";
    static final String PREF_USER_LOGGEDIN_STATUS = "biometric_login_status";
    static final String PREF_USER_BIOMETRIC_LOGIN_STATUS = "logged_in_status";
    static final String PREF_USER_IMAGE = "Current_User_Image";
    static final String PREF_USER_Password = "Current_User_Password";
    static final String PREF_USER_EMAIL = "Current_User_Email";

    static final String PREF_Login_With_Social = "logged_WITH_Social";
    static final String PREF_Provider_Id = "logged_provider_id";
    static final String PREF_ProviderName = "logged_provider_name";


    static final String PREF_LOGGEDIN_USER_FullName = "USER_FULL_NAME";
    static final String PREF_LOGGEDIN_USER_ADDRESS = "USER_ADDRESS";
    static final String PREF_LOGGEDIN_USER_PHONE = "USER_PHONE";

    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setBuildVersion(Context ctx, int version)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_BUILD_VERSION, version);
        editor.apply();
        editor.commit();
    }

    public static int getBuildVersion(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_BUILD_VERSION, 0);
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

    public static void setUserName(Context ctx, String fullName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_FullName, fullName);
        editor.apply();
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_FullName, "No Name");
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

    public static void setUserEmail(Context ctx, String email)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, email);
        editor.apply();
        editor.commit();
    }

    public static String getUserEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "no");
    }

    public static void setUserAddress(Context ctx, String fullName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_ADDRESS, fullName);
        editor.apply();
        editor.commit();
    }

    public static String getUserAddress(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_ADDRESS, "no");
    }

    public static void setUserPhoneNo(Context ctx, String fullName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_PHONE, fullName);
        editor.apply();
        editor.commit();
    }

    public static String getUserPhoneNo(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_PHONE, "no");
    }

    public static void setUserPassword(Context ctx, String password)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_Password, password);
        editor.apply();
        editor.commit();
    }

    public static String getUserPassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_Password, "no");
    }


    public static void setUserImage(Context ctx, String imageName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_IMAGE, imageName);
        editor.apply();
        editor.commit();
    }

    public static String getUserImage(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_IMAGE, "No Image");
    }

    public static void setBiometricLoginState(Context ctx, boolean status)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_BIOMETRIC_LOGIN_STATUS, status);
        editor.commit();
    }

    public static boolean getBiometricLoginState(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_BIOMETRIC_LOGIN_STATUS, false);
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.commit();
    }

    //saving a state if user is login with socialmedia
    public static boolean getLoginWithSocial(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_Login_With_Social, false);
    }

    public static void setUserLoginWithSocial(Context ctx, boolean loginWithName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_Login_With_Social, loginWithName);
        editor.commit();
    }

    public static void setProviderId(Context ctx, String providerId)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_Provider_Id, providerId);
        editor.apply();
        editor.commit();
    }

    public static String getProviderId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_Provider_Id, "no");
    }

    public static void setProviderName(Context ctx, String providerName)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ProviderName, providerName);
        editor.apply();
        editor.commit();
    }

    public static String getProviderName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_ProviderName, "no");
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    //clear the memory of the shared prefrences
    public static void clearLoginState(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_TOKEN);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.commit();
    }
    //clear the memory of the shared prefrences
    public static void clearAllPreferences(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_TOKEN);
        editor.remove(PREF_LOGGEDIN_USER_ID);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.remove(PREF_USER_IMAGE);
        editor.remove(PREF_LOGGEDIN_USER_FullName);
        editor.commit();
    }
}