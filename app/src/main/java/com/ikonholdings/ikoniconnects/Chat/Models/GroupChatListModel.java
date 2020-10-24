package com.ikonholdings.ikoniconnects.Chat.Models;

import java.util.List;

public class GroupChatListModel {

    private String group_Name,creator_Id, group_Profile, groupId;

    private List<Integer> group_User_Ids;

    public GroupChatListModel() {
        //required
    }

    public GroupChatListModel(String group_Name, List<Integer> group_User_Ids, String group_Profile, String creator_Id, String groupId) {
        this.group_Name = group_Name;
        this.groupId = groupId;
        this.group_User_Ids = group_User_Ids;
        this.group_Profile = group_Profile;
        this.creator_Id = creator_Id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroup_Name() {
        return group_Name;
    }

    public void setGroup_Name(String group_Name) {
        this.group_Name = group_Name;
    }

    public String getCreator_Id() {
        return creator_Id;
    }

    public void setCreator_Id(String creator_Id) {
        this.creator_Id = creator_Id;
    }

    public List<Integer> getGroup_User_Ids() {
        return group_User_Ids;
    }

    public void setGroup_User_Ids(List<Integer> group_User_Ids) {
        this.group_User_Ids = group_User_Ids;
    }

    public String getGroup_Profile() {
        return group_Profile;
    }

    public void setGroup_Profile(String group_Profile) {
        this.group_Profile = group_Profile;
    }
}