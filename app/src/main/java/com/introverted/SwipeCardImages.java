package com.introverted;

public class SwipeCardImages {
    private int id;
    private String saveLocation;

    public SwipeCardImages(int id, String saveLocation) {
        this.id = id;
        this.saveLocation = saveLocation;
    }

    public int getId() {
        return id;
    }

    public String getSaveLocation() {
        return saveLocation;
    }
}
