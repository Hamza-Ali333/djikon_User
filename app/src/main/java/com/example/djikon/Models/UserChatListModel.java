package com.example.djikon.Models;

public class UserChatListModel {

    private  String id, dj_Name,  imageUrl,key;
    private  String talkTime,last_send_msg;

    public UserChatListModel() {
    }

    //this constructor only for id , dJName, imageUrl
    public UserChatListModel(String id, String dj_Name, String imageUrl,String key) {
        this.id = id;
        this.dj_Name = dj_Name;
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getId() {
        return id;
    }

}
