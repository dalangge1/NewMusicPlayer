package com.example.newmusicplayer.bean;

public class AlbumDetailBean {
    private String coverImgUrl;
    private String name;
    private Creator creator;
    private Tracks[] tracksList;

    public Tracks[] getTracksList() {
        return tracksList;
    }

    public void setTracksList(Tracks[] tracksList) {
        this.tracksList = tracksList;
    }


    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
