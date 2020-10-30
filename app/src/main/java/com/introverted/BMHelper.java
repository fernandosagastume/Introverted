package com.introverted;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class BMHelper {
    Bitmap ppBM;
    private List<Bitmap> bitmap = new ArrayList<>();
    private static final BMHelper instance = new BMHelper();
    private static final BMHelper instance_EP = new BMHelper();
    private static final BMHelper instance_Pl = new BMHelper();

    public void setBitmap(Bitmap bitmap) {
        this.bitmap.add(bitmap);
    }

    public int getBipmapCount(){
        return this.bitmap.size();
    }

    public static BMHelper getInstance_PL() {
        return instance_Pl;
    }

    public static BMHelper getInstance_EP() {
        return instance_EP;
    }

    public BMHelper() {
    }

    public Bitmap getPpBM() {
        return ppBM;
    }

    public void setPpBM(Bitmap ppBM) {
        this.ppBM = ppBM;
    }

    public List<Bitmap> getBitmap() {
        return bitmap;
    }

    public static BMHelper getInstance() {
        return instance;
    }

    public void setBitmap(int index, Bitmap bitmap) {
        this.bitmap.add(index,bitmap);
    }
}
