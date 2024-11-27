package com.group21.NutriFit.Model;

import java.io.Serializable;

public class Activity implements Serializable {
    private int activityID;
    private int typeID;
    private int duration;
    private double caloriesBurned;
    private int dateUnix;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getDateUnix() {
        return dateUnix;
    }

    public void setDateUnix(int dateUnix) {
        this.dateUnix = dateUnix;
    }


}
