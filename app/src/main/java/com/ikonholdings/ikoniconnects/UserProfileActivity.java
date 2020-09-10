package com.ikonholdings.ikoniconnects;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.CountriesList;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.NetworkChangeReceiver;
import com.ikonholdings.ikoniconnects.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects.GlobelClasses.PreferenceData;
import com.ikonholdings.ikoniconnects.ResponseModels.DjAndUserProfileModel;
import com.ikonholdings.ikoniconnects.ResponseModels.SuccessErrorModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserProfileActivity extends AppCompatActivity {

    private EditText edt_FirstName, edt_LastName, edt_Email, edt_Phone_No, edt_Address;
    private AutoCompleteTextView edt_Location;
    private Button btn_Update_Profile;
    private Spinner mSpinner;
    private ImageView img_Profile;

    private RelativeLayout rlt_AboutApp, rlt_Setting, rlt_Disclosures;
    private ConstraintLayout rlt_Parent;
    private Switch swt_subcribeState;
    private ProgressBar mProgressBar;
    private TextView msg;

    private ProgressBar progressBarProfile;

    private Retrofit retrofit;
    private JSONApiHolder jsonApiHolder;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private String[] genderArray = {"Select Gender", "Male", "Female", "Other"};//for sippiner adapter
    private String FirstName, LastName;

    private String SelectedGender = "Select Gender";
    private String PhoneNo = "no";
    private String Address = "no";
    private String Profile;
    private static boolean isHavePassword;

    private String[] serverData;
    private String[] newData;


    private static final int IMAGE_PICK_GALLARY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;


    private Bitmap bitmap;
    private Uri Image_uri;
    private Boolean isProfileChange = false;//0 means user not selected new image , 1 means user change his/her profile
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
        createReferences();
        rlt_Parent.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        msg.setVisibility(View.VISIBLE);

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);

        getUserDataFromServer();

        swt_subcribeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_gender_spinner, R.id.genders, genderArray);

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

        rlt_AboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutAppDialogue();
            }
        });

        rlt_Disclosures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDisclusorDialog();
            }
        });

        rlt_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, ProfileSettingActivity.class);
                i.putExtra("password",isHavePassword);
                startActivity(i);
            }
        });


        btn_Update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInfoRight()){
                    if(isDataChange()){
                        updateProfile();
                    }else {
                        Toast.makeText(UserProfileActivity.this, "Already Updated", Toast.LENGTH_SHORT).show();
                    }
            }

            }
        });

        img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( PermissionHelper.checkDefaultPermissions(UserProfileActivity.this)) {
                    showImageImportDailog();
                }else {
                    PermissionHelper.managePermissions(UserProfileActivity.this);
                }
            }
        });

        ArrayList<String> countryArrayList = new ArrayList<String>(Arrays.asList(CountriesList.getCountry()));

        ArrayAdapter<String> Cadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArrayList);

        edt_Location.setAdapter(Cadapter);///HERE YOUR_LIST_VIEW IS YOUR LISTVIEW NAME


    }//onCreate


    private void openAboutAppDialogue() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoge_about_app, null);

        ImageView img_close = view.findViewById(R.id.close);

        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void openDisclusorDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialoge_disclusore, null);

        ImageView img_close = view.findViewById(R.id.close);

        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.show();

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


    private void getUserDataFromServer() {
        retrofit = ApiClient.retrofit(this);
        jsonApiHolder = retrofit.create(JSONApiHolder.class);
        String relativeURL = "user/" + PreferenceData.getUserId(this);
        Call<DjAndUserProfileModel> call = jsonApiHolder.getDjOrUserProfile(relativeURL);

        call.enqueue(new Callback<DjAndUserProfileModel>() {
            @Override
            public void onResponse(Call<DjAndUserProfileModel> call, Response<DjAndUserProfileModel> response) {
                if (response.isSuccessful()) {
                    DjAndUserProfileModel data = response.body();
                    FirstName = data.getFirstname();
                    LastName = data.getLastname();
                    edt_Email.setText(data.getEmail());
                    Address = data.getLocation();
                    PhoneNo = data.getContact();
                    SelectedGender =data.getGender();
                    Profile = data.getProfile_image();
                    isHavePassword = data.getPassword();

                    for (int j = 0; j < genderArray.length - 1; j++) {
                        if (genderArray[j].equals(SelectedGender)) {
                            mSpinner.setSelection(j);
                        }
                    }

                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            rlt_Parent.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);
                            setDataInToViews();
                        }
                    }));

                } else {
                    rlt_Parent.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    msg.setVisibility(View.GONE);
                   alertDialog = DialogsUtils.showResponseMsg(UserProfileActivity.this,false);
                }
            }

            @Override
            public void onFailure(Call<DjAndUserProfileModel> call, Throwable t) {
                alertDialog = DialogsUtils.showResponseMsg(UserProfileActivity.this,false);
            }
        });
    }

    private void updateProfile() {
        progressDialog = DialogsUtils.showProgressDialog(UserProfileActivity.this,
                "Uploading",
                "Please Wait...");
        String userId = PreferenceData.getUserId(UserProfileActivity.this);
        retrofit = ApiClient.retrofit(this);
        MultipartBody.Part filePart = null;

        if(Image_uri != null){
            File file = new File(Image_uri.getPath());
            filePart = MultipartBody.Part.createFormData("image",
                    file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"),
                edt_FirstName.getText().toString());
        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"),
                edt_LastName.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),
                edt_Phone_No.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"),
                edt_Location.getText().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"),
                SelectedGender);

        jsonApiHolder = retrofit.create(JSONApiHolder.class);

        Call<SuccessErrorModel> uploadCall = jsonApiHolder.UpdateProfileWithImage(
                    "update_profile/" + userId,
                    filePart,
                    firstname,
                    lastName,
                    phone,
                    gender,
                    location
            );

        uploadCall.enqueue(new Callback<SuccessErrorModel>() {
            @Override
            public void onResponse(Call<SuccessErrorModel> call, Response<SuccessErrorModel> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    //save new image name in to the preferences
                    PreferenceData.setUserImage(UserProfileActivity.this,response.body().getSuccess());
                    PreferenceData.setUserAddress(UserProfileActivity.this,edt_Address.getText().toString());
                    PreferenceData.setUserPhoneNo(UserProfileActivity.this,edt_Phone_No.getText().toString());
                    PreferenceData.setUserName(UserProfileActivity.this,
                            edt_FirstName.getText().toString()+" "+edt_LastName.getText().toString());

                    DialogsUtils.showSuccessDialog(UserProfileActivity.this,"Successful",
                            "Profile Successfully Updated",false);
                } else {
                    progressDialog.dismiss();
                    DialogsUtils.showResponseMsg(UserProfileActivity.this,false);
                }
            }

            @Override
            public void onFailure(Call<SuccessErrorModel> call, Throwable t) {
                progressDialog.dismiss();
                DialogsUtils.showResponseMsg(UserProfileActivity.this,true);
            }
        });
    }


    private boolean isInfoRight() {
        boolean result;
        if (edt_FirstName.getText().toString().trim().isEmpty()) {
            edt_FirstName.setError("Required");
            edt_FirstName.requestFocus();
            result = false;
        } else if (edt_FirstName.getText().toString().trim().isEmpty()) {
            edt_LastName.setError("Required");
            edt_LastName.requestFocus();
            result = false;
        } else if (!edt_Phone_No.getText().toString().trim().isEmpty() && edt_Phone_No.getText().length() < 11) {
            edt_Phone_No.setError("Phone No Should be Contain 11 Digits");
            edt_Phone_No.requestFocus();
            result = false;
        } else {
            assainValue();
            result = true;
        }
        return result;
    }

    private void assainValue() {
        FirstName = edt_FirstName.getText().toString().trim();
        LastName = edt_LastName.getText().toString().trim();

        if (!edt_Phone_No.getText().toString().trim().isEmpty()) {
            PhoneNo = edt_Phone_No.getText().toString().trim();
        }
        if (!edt_Location.getText().toString().trim().isEmpty()) {
            Address = edt_Location.getText().toString().trim();
        }

        newData = new String[]{FirstName, LastName, PhoneNo, SelectedGender, Address};
    }

    private boolean isDataChange() {
        boolean result = false;
        for (int i = 0; i < 5; i++) {
            if (!serverData[i].equals(newData[i])) {
                serverData[i] = newData[i];
                result = true;
                break;
            }
        }
        if(isProfileChange){
           result = true;
        }
        return result;
    }

    private void setDataInToViews() {
        serverData = new String[]{FirstName, LastName, PhoneNo, SelectedGender, Address};

        if(!Profile.equals("no") && Profile != null){
            progressBarProfile.setVisibility(View.VISIBLE);
            Picasso.get().load(ApiClient.Base_Url+"post_images/" + Profile)
                    .placeholder(R.drawable.progressbar)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_avatar)
                    .into(img_Profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                           progressBarProfile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                           progressBarProfile.setVisibility(View.GONE);
                        }
                    });
        }

        edt_FirstName.setText(FirstName);
        edt_LastName.setText(LastName);

        if (!PhoneNo.equals("no")){
            edt_Phone_No.setText(PhoneNo);
            PreferenceData.setUserAddress(UserProfileActivity.this,PhoneNo);
        }

        if (!Address.equals("no")) {
            edt_Location.setText(Address);
            PreferenceData.setUserPhoneNo(UserProfileActivity.this,Address);
        }
    }

    //image import
    private void showImageImportDailog() {
        String[] items = {"Camera", "Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(UserProfileActivity.this);
        dailog.setTitle("Select Image");
        dailog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 1) {
                    //pick Gallary
                    pickGallary();
                }
                if (which == 0) {
                    //pick Camera
                    pickCamera();
                }
            }
        });
        dailog.create().show();
    }

    private void pickCamera() {
        //Intent to take Image for camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pic");//title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text");//discription of the picture
        Image_uri = this.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickGallary() {
        //  intent for the Image from Gallary
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, IMAGE_PICK_GALLARY_REQUEST_CODE);
    }

    //handle Request for permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200){
            if (grantResults.length > 0) {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (!cameraAccepted || !storageAccepted) {
                    PermissionHelper.showPermissionAlert(UserProfileActivity.this);
                }
            }
        }
    }

    //Handle Image Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get selected image Image
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
                CropImage.activity(Image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UserProfileActivity.this);
            }
            //from gallary
            if (requestCode == IMAGE_PICK_GALLARY_REQUEST_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            //getcroped Image
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Image_uri);
                    //img_Profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.i("TAG", "onActivityResult: " + e.getMessage());
                }

                img_Profile.setImageURI(Image_uri);
                isProfileChange = true;//True Means that user chose a new iamge for profile
            }

        } else {
            Toast.makeText(this, "Image is not Selected try Again", Toast.LENGTH_SHORT).show();
        }

    }//onActivity Result

    public void createReferences() {
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
        progressBarProfile = findViewById(R.id.progressBarProfile);
        msg = findViewById(R.id.msg);

        rlt_AboutApp = findViewById(R.id.rlt_aboutApp);
        rlt_Disclosures = findViewById(R.id.rlt_disclosures);
        rlt_Setting = findViewById(R.id.rlt_setting);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }
}