package com.ikonholdings.ikoniconnects.GlobelClasses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ikonholdings.ikoniconnects.R;

public class PermissionHelper {

    private static final int PERMISSIONS_REQUEST_CODE = 200;

    private static  final String DEFAULT_PERMISSIONS[] = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};//this to permission are neccessary for use this app some feature perfectly

    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static void askForDefault(Activity activity){
        if(useRunTimePermissions()){
            ActivityCompat.requestPermissions(activity, DEFAULT_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    public static boolean checkDefaultPermissions(Activity activity) {
        boolean cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storagePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraPermission && storagePermission;
    }

    public static void managePermissions(Activity activity) {
        //if user not allowed then ask for permission
        if(!checkDefaultPermissions(activity)){
            askForDefault(activity);
        }
    }

    public static void goToAppSettings(Context activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static AlertDialog showPermissionAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Need Permissions");
        builder.setMessage("Please give permission to the for using this feature." +
                "\nThank You");
        builder.setCancelable(false);
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goToAppSettings(context);
            }
        })
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_alert);

        AlertDialog alertDialog; alertDialog = builder.show();
        return alertDialog;
    }

}
