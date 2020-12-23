package com.Ikonholdings.ikoniconnects.OnlineOfflineChat;

public class StatusModel {
    String status;
    String id;

    public StatusModel() {
    }

    public StatusModel(String id, String status) {
        this.status = status;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
