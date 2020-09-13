package com.ikonholdings.ikoniconnects.NavDrawerFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects.ApiHadlers.JSONApiHolder;
import com.ikonholdings.ikoniconnects.GlobelClasses.DialogsUtils;
import com.ikonholdings.ikoniconnects.GlobelClasses.PermissionHelper;
import com.ikonholdings.ikoniconnects.GlobelClasses.SaveFramImage;
import com.ikonholdings.ikoniconnects.R;
import com.ikonholdings.ikoniconnects.RecyclerView.RecyclerSocialMediaFrames;
import com.ikonholdings.ikoniconnects.ResponseModels.FramesModel;
import com.ikonholdings.ikoniconnects.UserProfileActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
//import static com.facebook.FacebookSdk.getApplicationContext;


public class SocialMediaShareFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private List<FramesModel> listOFImagesName;

    private ImageView img_Main;
    private ImageView img_Frame;
    private Button btn_Select_Image;
    private Button btn_Save_Image;

    private RelativeLayout view;

    //for permission handling and onActivity Result
    private static final int IMAGE_PICK_GALLARY_REQUEST_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 2000;

    private String cameraPermission[];
    private String storagePermission[];

    private Uri Image_uri;
    private TextView txt_Change_Image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v =  inflater.inflate(R.layout.fragment_social_media_share,container,false);
       createReferencse(v);

        listOFImagesName = new ArrayList<>();
        showLoadingDialogue();
        getFrames();


        btn_Select_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageImageDialog();
            }
        });

        txt_Change_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageImageDialog();
            }
        });

        btn_Save_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SaveFramImage saveFramImage = new SaveFramImage(getContext());

                saveFramImage.getscreenshot(view);//generate Image And Save in

            }
        });
        return v;
    }

    private void manageImageDialog () {
        if ( PermissionHelper.checkDefaultPermissions(getActivity())) {
            showImageImportDailog();
        }else {
            PermissionHelper.managePermissions(getActivity());
        }
    }

    private void createReferencse(View v) {
        view = v.findViewById(R.id.view);

        mRecyclerView = v.findViewById(R.id.recyclerView_frame);
        img_Main = v.findViewById(R.id.main_img);
        img_Frame =v.findViewById(R.id.fram_img);
        btn_Select_Image = v.findViewById(R.id.selectIamge);
        btn_Save_Image = v.findViewById(R.id.btn_save);

        txt_Change_Image = v.findViewById(R.id.changeimage);
    }

    private void getFrames(){
        Retrofit retrofit= ApiClient.retrofit(getContext());
        JSONApiHolder jsonApiHolder = retrofit.create(JSONApiHolder.class);
        Call<List<FramesModel>> call = jsonApiHolder.getFrames();

        call.enqueue(new Callback<List<FramesModel>>() {
            @Override
            public void onResponse(Call<List<FramesModel>> call, Response<List<FramesModel>> response) {

                if(response.isSuccessful()){
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.VISIBLE);

                    List<FramesModel> framesModelList= response.body();

                    if(framesModelList.isEmpty()) {
                        //if no data then show dialoge to user
                        DialogsUtils.showAlertDialog(getContext(),false,
                                "Note","No frames found against your followed Subscriber's");
                    } else{

                            sperationOfArray(framesModelList);
                    }

                }else {
                    alertDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);

                    DialogsUtils.showResponseMsg(getContext(),false);
                }
            }

            @Override
            public void onFailure(Call<List<FramesModel>> call, Throwable t) {
                alertDialog.dismiss();
                mRecyclerView.setVisibility(View.VISIBLE);
                DialogsUtils.showResponseMsg(getContext(),true);
            }
        });
    }

    private void showLoadingDialogue() {
        builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialoge_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.show();
    }

    private void sperationOfArray(List<FramesModel> list){
        for (int i = 0; i < list.size() ; i++) {
            String Gallery= list.get(i).getFrame();
            Gallery = Gallery.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "");
            String[] GalleryArray = Gallery.split(",");

            for (int j = 0; j <= GalleryArray.length - 1; j++) {
                listOFImagesName.add(new FramesModel(ApiClient.Base_Url+"post_images/" + GalleryArray[j]));
            }
        }

       buildRecyclerView(listOFImagesName);
    }

    private void buildRecyclerView(List<FramesModel> imageslist){
        mRecyclerView.setHasFixedSize(true);//if the recycler view not increase run time
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new RecyclerSocialMediaFrames(imageslist,getContext());
        ((RecyclerSocialMediaFrames) mAdapter).setOnItemClickListner(new RecyclerSocialMediaFrames.onItemClickListner() {
                    @Override
                    public void onClick(Bitmap bitmap) {
                        if(bitmap != null)
                        img_Frame.setImageBitmap(bitmap);
                        else
                            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                    }
                });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    //image import
    private void showImageImportDailog() {
        String[] items = {"Camera", "Gallary"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(getContext());
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
        Image_uri = getContext().getContentResolver()
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
                    PermissionHelper.showPermissionAlert(getContext());
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
                        .start(getContext(), this);
            }
            //from gallary
            if (requestCode == IMAGE_PICK_GALLARY_REQUEST_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }
            //getcroped Image
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Image_uri = result.getUri();

                img_Main.setImageURI(Image_uri);
            }
            btn_Select_Image.setVisibility(View.GONE);
        } else {
            btn_Select_Image.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Image is not Selected try Again", Toast.LENGTH_SHORT).show();
        }

    }//onActivity Result

}
