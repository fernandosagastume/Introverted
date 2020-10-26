package com.introverted;

import android.graphics.Bitmap;

public class MatchModel {
    private int id;
    private Bitmap image;
    private String name;

    public MatchModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.image = null;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
