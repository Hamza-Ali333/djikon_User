package com.ikonholdings.ikoniconnects;

public class LiveToArtist {
    private int img_live_artist;
    private String txt_LiveArtistName;

    public LiveToArtist(int img_live_artist, String txt_LiveArtistName) {
        this.img_live_artist = img_live_artist;
        this.txt_LiveArtistName = txt_LiveArtistName;
    }

    public int getImg_live_artist() {
        return img_live_artist;
    }

    public String getTxt_LiveArtistName() {
        return txt_LiveArtistName;
    }
}
