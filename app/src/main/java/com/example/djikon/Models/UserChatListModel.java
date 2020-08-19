package com.example.djikon.Models;

public class UserChatListModel {

    private  String dj_Id, dj_Name, dj_Uid, imageUrl, key;
    private  String talkTime,last_send_msg;

    public UserChatListModel() {
    }

    //this constructor only for id , dJName, imageUrl
    public UserChatListModel(String dj_Id, String dj_Uid, String dj_Name, String imageUrl, String key) {
        this.dj_Id = dj_Id;
        this.dj_Uid = dj_Uid;
        this.dj_Name = dj_Name;
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public String getDj_Uid() {
        return dj_Uid;
    }

    public void setDj_Uid(String dj_Uid) {
        this.dj_Uid = dj_Uid;
    }

    public void setDj_Id(String dj_Id) {
        this.dj_Id = dj_Id;
    }

    public void setDj_Name(String dj_Name) {
        this.dj_Name = dj_Name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getDj_Name() {
        return dj_Name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDj_Id() {
        return dj_Id;
    }

}
