package com.example.g2_nirav_sridheepan.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="users")
public class User {
    private String fullName;
    private String email_id;
    private String password;
    private String date;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public User(String fullName, String email_id, String password, String date) {
        this.fullName = fullName;
        this.email_id = email_id;
        this.password = password;
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email_id='" + email_id + '\'' +
                ", password='" + password + '\'' +
                ", date='" + date + '\'' +
                ", id=" + id +
                '}';
    }
}
