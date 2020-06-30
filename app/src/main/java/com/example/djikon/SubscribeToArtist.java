package com.example.djikon;

public class SubscribeToArtist {
    private int img_Subscribe_Artist;
    private String txt_SubscribeArtistName, txt_SubscribeArtistStatus;

    public SubscribeToArtist(int img_live_artist, String txt_SubscribeArtistName, String txt_SubscribeArtistStatus) {
        this.img_Subscribe_Artist = img_live_artist;
        this.txt_SubscribeArtistName = txt_SubscribeArtistName;
        this.txt_SubscribeArtistStatus = txt_SubscribeArtistStatus;
    }

    public int getImg_Subscribe_Artist() {
        return img_Subscribe_Artist;
    }

    public String getTxt_SubscribeArtistName() {
        return txt_SubscribeArtistName;
    }

    public String getTxt_SubscribeArtistStatus() {
        return txt_SubscribeArtistStatus;
    }
}
