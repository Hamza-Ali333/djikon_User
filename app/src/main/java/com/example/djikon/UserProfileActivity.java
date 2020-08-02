package com.example.djikon;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.security.AccessController.getContext;

public class UserProfileActivity extends AppCompatActivity  {


    private EditText edt_FirstName, edt_LastName, edt_Email, edt_Phone_No, edt_Address;
    private AutoCompleteTextView edt_Location;
    private Button btn_Update_Profile;
    private Spinner mSpinner;
    private ImageView img_Profile;

    private RelativeLayout  rlt_PaymentMethod, rlt_AboutApp, rlt_Setting ,rlt_Disclosures;
    private ConstraintLayout rlt_Parent;
    private Switch swt_subcribeState;
    private ProgressBar mProgressBar;
    private TextView msg;



    private static final String BASEURL = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/user/";
    private static final String UPDATEPROFILE = "http://ec2-54-161-107-128.compute-1.amazonaws.com/api/update_profile/";

    private PreferenceData preferenceData;

    private  Retrofit retrofit;
    private  JSONApiHolder jsonApiHolder;
    private ProgressDialog progressDialog;


    private String[] genderArray = {"Select Gender","Male", "Female", "Other"};
    private String FirstName, LastName;

    private String SelectedGender ="Select Gender";
    private String PhoneNo= "no";
    private String  Address = "no";

    private String[] serverData;
    private String[] newData;


    private static final int CAMERA_REQUEST_CODE = 300;
    private static final int STORAFGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLARY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    String cameraPermission[];
    String storagePermission[];
    private Bitmap bitmap;
    private Uri Image_uri;

    private File finalFile;

    private NetworkChangeReceiver mNetworkChangeReceiver;


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkChangeReceiver, filter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        createRefrences();
        rlt_Parent.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        msg.setVisibility(View.VISIBLE);
        preferenceData= new PreferenceData();

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);



        Thread downloadData = new Thread(new Runnable() {
            @Override
            public void run() {
                getUserDataFromServer();
            }
        });

        downloadData.start();



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //camerapermission
                cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //storagepermission
                storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

                //Ask for Required Permissions
                final String[] permissions = new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE
                };

                ActivityCompat.requestPermissions(UserProfileActivity.this, permissions, 123);
            }
        });
        thread.start();


        swt_subcribeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, R.id.genders, genderArray);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SelectedGender = genderArray[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
 
        rlt_PaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, PaymentMethodActivity.class);
                startActivity(i);
            }
        });

        rlt_AboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutAppDialogue();
            }
        });

        rlt_Disclosures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDisclusoreDialogue();
            }
        });

        rlt_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this,ProfileSettingActivity.class);
                startActivity(i);
            }
        });


        btn_Update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInfoRight()){
                    if(isDataChange()){

                        progressDialog = DialogsUtils.showProgressDialog(UserProfileActivity.this,"Uploading","Please Wait...");
                        Thread update = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                updateProfile(preferenceData.getUserId(UserProfileActivity.this));
                              //  uploadImage();
                            }
                        });
                        update.start();

                    }else {
                        Toast.makeText(UserProfileActivity.this, "Already Updated", Toast.LENGTH_SHORT).show();
                    }
            }

            }
        });

        img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageImportDailog();
            }
        });



        ArrayList<String> countryArrayList = new ArrayList<String>(Arrays.asList(CountriesList.getCountry()));

        ArrayAdapter<String> Cadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArrayList);

        edt_Location.setAdapter(Cadapter);///HERE YOUR_LIST_VIEW IS YOUR LISTVIEW NAME

        edt_Location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(UserProfileActivity.this, countryArrayList.get(i), Toast.LENGTH_SHORT).show();

            }
        });


    }//onCreate


    private void openAboutAppDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_about_app, null);

        ImageView img_close = view.findViewById(R.id.close);


        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog =  builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void openDisclusoreDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_disclusore, null);

        ImageView img_close = view.findViewById(R.id.close);

        builder.setView(view);
        builder.setCancelable(false);


        final AlertDialog alertDialog =  builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getUserDataFromServer(){

        retrofit = ApiResponse.retrofit(BASEURL,this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<ProfileModel> call = jsonApiHolder.getDjOrUserProfile(PreferenceData.getUserId(this));

        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if(response.isSuccessful()){


                    FirstName=response.body().getFirstname();
                    LastName=response.body().getLastname();

                    edt_Email.setText(response.body().getEmail());

                    Address = response.body().getLocation();
                    PhoneNo = response.body().getContact();
                    SelectedGender = response.body().getGender();

                    for (int j = 0; j <genderArray.length-1 ; j++) {
                        if(genderArray[j].equals(SelectedGender)){
                            mSpinner.setSelection(j);
                        }
                    }

                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {

                            rlt_Parent.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);
                            setDataInToFields();

                        }
                    }));

                }else {
                    rlt_Parent.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    msg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateProfile(String UserId){

        retrofit = ApiResponse.retrofit(BASEURL,this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);

//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
//                finalFile);
//        RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"),
//                edt_FirstName.getText().toString());
//        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"),
//                edt_LastName.getText().toString());
//        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),
//                edt_Phone_No.getText().toString());
//        RequestBody location = RequestBody.create(MediaType.parse("text/plain"),
//                edt_Location.getText().toString());
//        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"),
//               SelectedGender);

String img= imageToString();
        Call<SuccessErrorModel> call = jsonApiHolder.UpdateUserProfile(
                UserId,
                     img ,
                edt_FirstName.getText().toString(),
                edt_LastName.getText().toString(),
                edt_Phone_No.getText().toString(),
                SelectedGender,
                edt_Location.getText().toString()
               );

        call.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {
                if(response.isSuccessful()){
                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                    }));


                    Log.i("TAG", "onResponse: "+response.code());
                }else {
                    progressDialog.dismiss();
                    Log.i("TAG", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }

        private void uploadImage(){
            retrofit = ApiResponse.retrofit(BASEURL,this);

           // String path = Image_uri.getPath();

            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image","imageName",RequestBody.create(MediaType.parse("multipart/form-data"), finalFile));
            jsonApiHolder = retrofit.create(JSONApiHolder.class);

            File file = new File(Image_uri.getPath());
            Call<SuccessErrorModel> uploadCall = jsonApiHolder.uploadImage(preferenceData.getUserId(UserProfileActivity.this),filePart);
            uploadCall.enqueue(new Callback<SuccessErrorModel>() {
                @Override
                public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {

                    if(response.isSuccessful()){
                        Toast.makeText(UserProfileActivity.this, "Yes Got it", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        Log.i("TAG", "onResponse: "+response.code());
                        Toast.makeText(UserProfileActivity.this, "Again Fucked", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onFailure: "+t.getMessage());
                }
            });
        }


    private boolean isInfoRight () {
        boolean result;
        if (edt_FirstName.getText().toString().trim().isEmpty()) {
            edt_FirstName.setError("Required");
            edt_FirstName.requestFocus();
            result= false;
        } else if (edt_FirstName.getText().toString().trim().isEmpty()){
            edt_LastName.setError("Required");
            edt_LastName.requestFocus();
            result= false;
        }else if (!edt_Phone_No.getText().toString().trim().isEmpty() && edt_Phone_No.getText().length() < 11){
            edt_Phone_No.setError("Phone No Should be Contain 11 Digits");
            edt_Phone_No.requestFocus();
            result= false;
        }else {
            assainValue();
            result =true;
        }
        return result;
    }

    private void assainValue(){
        FirstName = edt_FirstName.getText().toString().trim();
        LastName = edt_LastName.getText().toString().trim();

        if (!edt_Phone_No.getText().toString().trim().isEmpty()){
            PhoneNo = edt_Phone_No.getText().toString().trim();
        }
        if (!edt_Location.getText().toString().trim().isEmpty()){
            Address = edt_Location.getText().toString().trim();
        }

        newData = new String[]{FirstName,LastName,PhoneNo,SelectedGender,Address};
    }


    private boolean isDataChange(){
        boolean result= false;
        for (int i = 0; i < 5; i++) {
            if(!serverData[i].equals(newData[i])){
                serverData[i]= newData[i];
                result= true;
            }
        }
        return result;
    }


    private void setDataInToFields(){
        serverData = new String[]{FirstName, LastName, PhoneNo , SelectedGender , Address};

        edt_FirstName.setText(FirstName);
        edt_LastName.setText(LastName);
        if(!PhoneNo.equals("no"))
            edt_Phone_No.setText(PhoneNo);
        if(!Address.equals("no"))
            edt_Location.setText(Address);
    }




    //image import
    private void showImageImportDailog(){

        String[] items ={"Camera","Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(UserProfileActivity.this);
        dailog.setTitle("Select Image");
        dailog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 1){
                    //camera run
                    if(!checkCameraPermissions()){
                        //RequestCamera Permission
                        requestPermissionCamera();
                    }
                }else {
                    //pick Camera
                    pickCamera();
                }
                if(which == 0){
                    //open Gallary
                    if(!checkStoragePermissions()){
                        //RequestStorage Permission
                        requestPermissionStorage();
                    }
                }else {
                    //pick Gallary
                    pickGallary();
                }
            }
        });
        dailog.create().show();
    }

    private boolean checkCameraPermissions(){
        boolean result = ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.CAMERA) ==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);


        return result && result1;
    }

    private boolean checkStoragePermissions(){
        boolean result = ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);


        return result;
    }

    private void requestPermissionCamera(){
        ActivityCompat.requestPermissions(UserProfileActivity.this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    private void requestPermissionStorage(){
        ActivityCompat.requestPermissions(UserProfileActivity.this,storagePermission,STORAFGE_REQUEST_CODE);
    }

    private void pickCamera(){
        //Intent to take Image for camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pic");//title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");//discription of the picture
        Image_uri = this.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickGallary(){
        //  intent for the Image from Gallary
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery,IMAGE_PICK_GALLARY_REQUEST_CODE);
    }


    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickCamera();
                    }else {
                        Toast.makeText(UserProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAFGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickGallary();
                    }else {
                        Toast.makeText(UserProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    //Handle Image Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        //get selected image Image
        if(resultCode ==RESULT_OK) {

            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){
                CropImage.activity(Image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UserProfileActivity.this);
            }
            //from gallary
            if(requestCode == IMAGE_PICK_GALLARY_REQUEST_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            //getcroped Image
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();
                //img_Profile.setImageURI(Image_uri);
                //Image_uri = data.getData();//data is getting null have to check
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Image_uri);
                    //img_Profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.i("TAG", "onActivityResult: "+e.getMessage());
                }


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = new File(getRealPathFromURI(tempUri));

                img_Profile.setImageURI(tempUri);

            }



        }else {
            Toast.makeText(this, "Image is not Selected try Again", Toast.LENGTH_SHORT).show();
        }


    }//onActivity Result


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }





    //compress the image and decode it
    private  String imageToString () {
        //this veriable will contain all the bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //compress the bitmap into jpg formate
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        //convert the byte array output stream into array of byte
        byte[] imageByte = byteArrayOutputStream.toByteArray();

        //now encode the byte array
     return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }




    public void createRefrences(){

        edt_FirstName = findViewById(R.id.edt_User_First_Name);
        edt_LastName = findViewById(R.id.edt_User_LastName);
        edt_Email = findViewById(R.id.edt_UserEmail);
        edt_Phone_No = findViewById(R.id.edt_User_PhoneNo);
        edt_Location = findViewById(R.id.edt_User_Location);
        edt_Address = findViewById(R.id.edt_UserAddress);
        mSpinner = findViewById(R.id.spinner);

        img_Profile = findViewById(R.id.img_UserImage);

        btn_Update_Profile = findViewById(R.id.btn_UpdateProfile);

        swt_subcribeState = findViewById(R.id.swt_subscribeState);
        rlt_Parent = findViewById(R.id.parent);
        mProgressBar = findViewById(R.id.progress_circular);
        msg = findViewById(R.id.msg);


        rlt_AboutApp = findViewById(R.id.rlt_aboutApp);
        rlt_Disclosures = findViewById(R.id.rlt_disclosures);
        rlt_PaymentMethod = findViewById(R.id.rlt_paymentMethod);
        rlt_Setting = findViewById(R.id.rlt_setting);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}

