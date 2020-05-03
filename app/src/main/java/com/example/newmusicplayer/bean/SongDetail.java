package com.example.newmusicplayer.bean;

public class SongDetail {
    private String name = "";
    private Artist ar = new Artist();
    private Album al = new Album();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getAr() {
        return ar;
    }

    public void setAr(Artist ar) {
        this.ar = ar;
    }

    public Album getAl() {
        return al;
    }

    public void setAl(Album al) {
        this.al = al;
    }
}
