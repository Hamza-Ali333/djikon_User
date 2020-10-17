package com.ikonholdings.ikoniconnects.ResponseModels;

public class UserChatListModel {

    private  String subscriber_Id, subscriber_Name, imgProfileUrl, key;
    private  String talkTime,last_send_msg;

    private String status;

    public UserChatListModel() {
    }

    //this constructor only for id , dJName, imageUrl
    public UserChatListModel(String subscriber_Id,
                             String subscriber_Name,
                             String imgProfileUrl,
                             String status,
                             String key) {
        this.subscriber_Id = subscriber_Id;
        this.subscriber_Name = subscriber_Name;
        this.imgProfileUrl = imgProfileUrl;
        this.status = status;
        this.key = key;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public void setsubscriber_Id(String subscriber_Id) {
        this.subscriber_Id = subscriber_Id;
    }

    public void setsubscriber_Name(String subscriber_Name) {
        this.subscriber_Name = subscriber_Name;
    }

    public void setimgProfileUrl(String imageUrl) {
        this.imgProfileUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(String talkTime) {
        this.talkTime = talkTime;
    }

    public String getLast_send_msg() {
        return last_send_msg;
    }

    public void setLast_send_msg(String last_send_msg) {
        this.last_send_msg = last_send_msg;
    }

    public String getSubscriber_Name() {
        return subscriber_Name;
    }

    public String getImgProfileUrl() {
        return imgProfileUrl;
    }

    public String getSubscriber_Id() {
        return subscriber_Id;
    }

}
