package com.example.g2_nirav_sridheepan.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tasker implements Serializable {
    private String name;
    private Integer age;
    private String address;
    private String task;
    private String latitude;
    private String longitude;
    private String id;
    private String image_url;
    private Integer rate;
    private Double distance = 0.0;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getTask() {
        return task;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public Integer getRate() {
        return rate;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Tasker{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", task='" + task + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", id='" + id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", rate=" + rate +
                ", distance=" + distance +
                '}';
    }
}
