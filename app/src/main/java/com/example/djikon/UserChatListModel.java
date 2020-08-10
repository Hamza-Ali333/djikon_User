package com.example.djikon;

public class UserChatListModel {
    private int img_msg_sender;
    private  String msg_Sender_Name, msg_Recieved_Time, msg_last_send, msg_UnRead;

    public UserChatListModel(int img_msg_sender, String msg_UnRead, String msg_Sender_Name, String msg_last_send, String msg_Recieved_Time) {
        this.img_msg_sender = img_msg_sender;
        this.msg_UnRead = msg_UnRead;
        this.msg_Sender_Name = msg_Sender_Name;
        this.msg_Recieved_Time = msg_Recieved_Time;
        this.msg_last_send = msg_last_send;
    }

    public int getImg_msg_sender() {
        return img_msg_sender;
    }

    public String getMsg_UnRead() {
        return msg_UnRead;
    }

    public String getMsg_Sender_Name() {
        return msg_Sender_Name;
    }

    public String getMsg_Recieved_Time() {
        return msg_Recieved_Time;
    }

    public String getMsg_last_send() {
        return msg_last_send;
    }
}
