package com.example.djikon.Models;

public class UserChatListModel {

    private  String id, dj_Name,  imageUrl;
    private  String talkTime,last_send_msg;


    public UserChatListModel() {
    }

    //this constructor only for id , dJName, imageUrl
    public UserChatListModel(String id, String dj_Name, String imageUrl) {
        this.id = id;
        this.dj_Name = dj_Name;
        this.imageUrl = imageUrl;
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
