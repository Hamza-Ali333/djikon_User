package com.example.djikon.Models;

public class ChatModel {
    public String sender,
    receiver,
    message,
            time_stemp;


    public ChatModel() {
        //required for firebase
    }

    public ChatModel(String sender, String receiver, String message, String time_stemp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time_stemp = time_stemp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
}
