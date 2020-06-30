package com.example.djikon;

public class BookingHistory {

    private int img_booker_image;
    private  String txt_booker_Name, txt_booked_date,
            txt_Booked_Event_Tilte, txt_booked_event_discription, txt_booking_charges;


    public BookingHistory(int img_booker_image,
                          String txt_booker_Name,
                          String txt_booked_date,
                          String txt_Booked_Event_Tilte,
                          String txt_booked_event_discription,
                          String txt_booking_charges) {

        this.img_booker_image = img_booker_image;
        this.txt_booker_Name = txt_booker_Name;
        this.txt_booked_date = txt_booked_date;
        this.txt_Booked_Event_Tilte = txt_Booked_Event_Tilte;
        this.txt_booked_event_discription = txt_booked_event_discription;
        this.txt_booking_charges = txt_booking_charges;
    }


    public int getImg_booker_image() {
        return img_booker_image;
    }

    public String getTxt_booker_Name() {
        return txt_booker_Name;
    }

    public String getTxt_booked_date() {
        return txt_booked_date;
    }

    public String getTxt_Booked_Event_Tilte() {
        return txt_Booked_Event_Tilte;
    }

    public String getTxt_booked_event_discription() {
        return txt_booked_event_discription;
    }

    public String getTxt_booking_charges() {
        return txt_booking_charges;
    }
}
