package com.ikonholdings.ikoniconnects.Chat.Models;

import java.util.List;

public class ManytoManyChatModel {

    private List<Integer> receivers;
    private String sender_Id, message, time_stemp, image, sender_Name, key;


    public ManytoManyChatModel() {
        //required
    }

    public ManytoManyChatModel(List<Integer> receivers, String sender_Id, String message, String time_stemp, String image, String sender_Name, String key) {
        this.receivers = receivers;
        this.sender_Id = sender_Id;
        this.message = message;
        this.time_stemp = time_stemp;
        this.image = image;
        this.sender_Name = sender_Name;
        this.key = key;
    }

    public List<Integer> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Integer> receivers) {
        this.receivers = receivers;
    }

    public String getSender_Id() {
        return sender_Id;
    }

    public void setSender_Id(String sender_Id) {
        this.sender_Id = sender_Id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime_stemp() {
        return time_stemp;
    }

    public void setTime_stemp(String time_stemp) {
        this.time_stemp = time_stemp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSender_Name() {
        return sender_Name;
    }

    public void setSender_Name(String sender_Name) {
        this.sender_Name = sender_Name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
