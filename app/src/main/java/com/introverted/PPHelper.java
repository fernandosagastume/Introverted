package com.introverted;

import java.util.ArrayList;
import java.util.List;

public class PPHelper {
    int conter;
    List<String> name = new ArrayList<>();
    List<String> city = new ArrayList<>();
    List<String> distance = new ArrayList<>();
    List<String> sex = new ArrayList<>();
    private static final PPHelper instance = new PPHelper();
    private static final PPHelper instance_no = new PPHelper();

    public PPHelper() {
    }

    public static PPHelper getInstance() {
        return instance;
    }

    public static PPHelper getInstanceNO() {
        return instance_no;
    }

    public int getConter() {
        return conter;
    }

    public void setConter(int conter) {
        this.conter = conter;
    }

    public void setName(String name) {
        this.name.add(name);
    }

    public void setCity(String city) {
        this.city.add(city);
    }

    public void setDistance(String distance) {
        this.distance.add(distance);
    }

    public void setSex(String sex) {
        this.sex.add(sex);
    }
}
