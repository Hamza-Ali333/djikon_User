package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {

    private static final int CAMERA_REQUEST_CODE = 300;
    private static final int STORAGE_REQUEST_CODE = 400;

    private static  final String CAMERA_PERMISSION[] = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String STORAGE_PERMISSION[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final  String[] ALL_PERMISSION = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE };

    public static void askForDefault(Activity activity){
        ActivityCompat.requestPermissions(activity, ALL_PERMISSION, 123);
    }

    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean checkCameraPermissions(Activity activity) {
        boolean result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    public static boolean checkStoragePermissions(Activity activity) {
        boolean result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public static void requestPermissionCamera(Activity activity) {
        ActivityCompat.requestPermissions(activity, CAMERA_PERMISSION, CAMERA_REQUEST_CODE);
    }

    public static void requestPermissionStorage(Activity activity) {
        ActivityCompat.requestPermissions(activity, STORAGE_PERMISSION, STORAGE_REQUEST_CODE);
    }

}
