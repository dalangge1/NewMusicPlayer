package com.example.newmusicplayer.bean;


public class AlbumRecommendBean {


    private String name;
    private String coverImgUrl;
    private Long id;
    private Creator creator;

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Creator getCreator() {
        return creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getName() {
        return name;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

}
