package com.Ikonholdings.ikoniconnects.Chat.Notification;

public class GroupMembers {

    public Data data;//Data Class ObJect
    public String[] registration_ids;

    public GroupMembers(Data data, String[] registration_ids) {
        this.data = data;
        this.registration_ids = registration_ids;
    }

}
