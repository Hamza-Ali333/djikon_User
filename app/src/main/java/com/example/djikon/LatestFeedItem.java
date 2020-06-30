package com.example.djikon;

public class LatestFeedItem {

    private int Img_UploaderProfile,
            img_FeedImage;


    private String txt_UploaderName,
            txt_UploadTime,
            txt_Description,
            txt_LikesNo,
            txt_ChatNo;

    public LatestFeedItem(int img_UploaderProfile, int img_FeedImage,

                          String txt_UploaderName, String txt_UploadTime,
                          String txt_Description, String txt_LikesNo,
                          String txt_ChatNo) {

        this.Img_UploaderProfile = img_UploaderProfile;
        this.img_FeedImage = img_FeedImage;


        this.txt_UploaderName = txt_UploaderName;
        this.txt_UploadTime = txt_UploadTime;
        this.txt_Description = txt_Description;
        this.txt_LikesNo = txt_LikesNo;
        this.txt_ChatNo = txt_ChatNo;
    }

    public int getImg_UploaderProfile() {
        return Img_UploaderProfile;
    }

    public int getImg_FeedImage() {
        return img_FeedImage;
    }

    public String getTxt_UploaderName() {
        return txt_UploaderName;
    }

    public String getTxt_UploadTime() {
        return txt_UploadTime;
    }

    public String getTxt_Description() {
        return txt_Description;
    }

    public String getTxt_LikesNo() {
        return txt_LikesNo;
    }

    public String getTxt_ChatNo() {
        return txt_ChatNo;
    }
}
