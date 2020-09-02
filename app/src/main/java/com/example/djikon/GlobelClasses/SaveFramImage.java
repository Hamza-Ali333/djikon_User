package com.example.djikon.GlobelClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SaveFramImage {

    Context context;
    Bitmap bitmap;
    private File imagePath;

    public SaveFramImage(Context context) {
        this.context = context;

    }

    //use this method to get the bitmap
    public void getscreenshot(View view) {
        View v = view;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());

        //this will save imgae in storage
        saveBitmap();
    }

    //saving screenshot in the storage
    public void saveBitmap() {
        String mSecond = String.valueOf(System.currentTimeMillis());
        imagePath = new File(Environment.getExternalStorageDirectory() +"/"+mSecond+"scrnshot.png"); ////File imagePath
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "Image Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
            Toast.makeText(context, "File" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
            Toast.makeText(context, "Io" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //share Screenshot
    public void shareImage() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath.getAbsolutePath()));
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

}