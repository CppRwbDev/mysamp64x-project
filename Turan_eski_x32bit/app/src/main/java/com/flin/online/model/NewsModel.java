package com.flin.online.model;


import android.graphics.drawable.Drawable;

public class NewsModel {

    public int image;
    public Drawable imageDrw;
    public String name;
    public String data;
    public String description;
    public Integer counter = null;
    public String imageURL = null;
    public Integer type = 0;
    public String url = null;


    public NewsModel() {
    }


    public NewsModel(String name, String data, String description, String imageURL, int type, String urlLink) {
        this.name = name;
        this.data = data;
        this.description = description;
        this.imageURL = imageURL;
        this.url = urlLink;
        this.type = type;
    }

}
