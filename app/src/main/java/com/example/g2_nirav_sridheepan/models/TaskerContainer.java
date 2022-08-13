package com.example.g2_nirav_sridheepan.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TaskerContainer {
    private @SerializedName("taskers") ArrayList<Tasker> taskersList;

    public ArrayList<Tasker> getTaskersList() {
        return taskersList;
    }

    @Override
    public String toString() {
        return "TaskerContainer{" +
                "taskersList=" + taskersList +
                '}';
    }
}
