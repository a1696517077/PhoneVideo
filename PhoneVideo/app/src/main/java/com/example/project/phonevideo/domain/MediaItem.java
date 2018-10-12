package com.example.project.phonevideo.domain;

import java.io.Serializable;

public class MediaItem implements Serializable{


    private String name;
    private long Druation;
    private long Size;
    private String Data;
    private String Artist;
    private String desc;
    private String imageUrl;


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDruation() {
        return Druation;
    }

    public void setDruation(long druation) {
        Druation = druation;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", Druation=" + Druation +
                ", Size=" + Size +
                ", Data='" + Data + '\'' +
                ", Artist='" + Artist + '\'' +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
