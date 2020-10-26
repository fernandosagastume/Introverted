package com.introverted;

import android.graphics.Bitmap;

import com.android.volley.toolbox.StringRequest;

public class cardModel {
    private int id;
    private Bitmap image;
    private String name, age, city, distance;

    public cardModel(){
    }

    public cardModel(int id, String name, String age, String city){
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.image = null;
    }

    public int getId() {
        return id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
