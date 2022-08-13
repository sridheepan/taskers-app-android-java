package com.example.g2_nirav_sridheepan.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName="hires")
public class Hire implements Serializable {
    private String tasker_name;
    private String tasker_type;
    private String tasker_imageURL;
    private int user_id;
    private String hireDate;
    private String hireTime;
    private String promocode;
    private boolean isPromoHire;
    private int totalAmount;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Hire(String tasker_name, String tasker_type, String tasker_imageURL, int user_id, String hireDate, String hireTime, String promocode, boolean isPromoHire, int totalAmount) {
        this.tasker_name = tasker_name;
        this.tasker_type = tasker_type;
        this.tasker_imageURL = tasker_imageURL;
        this.user_id = user_id;
        this.hireDate = hireDate;
        this.hireTime = hireTime;
        this.promocode = promocode;
        this.isPromoHire = isPromoHire;
        this.totalAmount = totalAmount;
    }

    public String getTasker_name() {
        return tasker_name;
    }

    public String getTasker_type() {
        return tasker_type;
    }

    public String getTasker_imageURL() {
        return tasker_imageURL;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getHireTime() {
        return hireTime;
    }

    public String getPromocode() {
        return promocode;
    }

    public boolean isPromoHire() {
        return isPromoHire;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void setHireTime(String hireTime) {
        this.hireTime = hireTime;
    }

    @Override
    public String toString() {
        return "Hire{" +
                "tasker_name='" + tasker_name + '\'' +
                ", tasker_type='" + tasker_type + '\'' +
                ", tasker_imageURL='" + tasker_imageURL + '\'' +
                ", user_id=" + user_id +
                ", hireDate='" + hireDate + '\'' +
                ", hireTime='" + hireTime + '\'' +
                ", promocode='" + promocode + '\'' +
                ", isPromoHire=" + isPromoHire +
                ", totalAmount=" + totalAmount +
                ", id=" + id +
                '}';
    }
}
