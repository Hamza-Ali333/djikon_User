package com.ikonholdings.ikoniconnects.Chat.Notification;

public class Sender {

    public Notification notification;//Data Class ObJect
    public String to;//receiver

    public Sender(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }
}
//public class Sender {
//
//    public Data data;//Data Class ObJect
//    public String to;
//
//    public Sender(Data data, String to) {
//        this.data = data;
//        this.to = to;
//    }
//}
