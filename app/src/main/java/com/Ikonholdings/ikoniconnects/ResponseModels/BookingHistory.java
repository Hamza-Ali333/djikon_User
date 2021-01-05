package com.Ikonholdings.ikoniconnects.ResponseModels;

public class BookingHistory {

   private  int id;
   private Integer service_id;
   private Integer sub_id;
   private String firstname;
   private String lastname;
   private String name;
   private String profile_image;
   private String price;
   private String address;
   private String start_date;
   private String end_date;
   private String created_at;
   private String end_time;
   private String status;

    public Integer getService_id() {
        return service_id;
    }

    public Integer getSub_id() {
        return sub_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProfile_image() {
        return profile_image;
    }
}
