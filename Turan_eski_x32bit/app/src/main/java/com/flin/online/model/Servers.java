package com.flin.online.model;


import android.graphics.drawable.Drawable;

public class Servers {

    public int image;
    public int server;
    public Drawable imageDrw;
    public String name;
    public String description;
    public boolean section = false;

    public Servers() {
    }

    public Servers(String name, String description, int server) {
        this.name = name;
        this.description = description;
        this.server = server;
    }

}
