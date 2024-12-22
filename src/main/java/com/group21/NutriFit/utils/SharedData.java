package com.group21.NutriFit.utils;

import com.group21.NutriFit.Model.Activity;
import com.group21.NutriFit.Model.Nutrition;
import com.group21.NutriFit.Model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedData {
    private static SharedData sharedInstance;
    private User currentUser = null;
    private List<Activity> activities = new ArrayList<>();
    private List<Nutrition> nutritions = new ArrayList<>();
    private String workouts ="";
    private LocalDateTime workoutTime;
    private double calorieIntake;
    private double calorieTarget;

    public SharedData(){

    }

    public double getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(double calorieTarget) {
        this.calorieTarget = calorieTarget;
    }

    public double getCalorieIntake() {
        return calorieIntake;
    }

    public void setCalorieIntake(double calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public Map<String, Object> getEncryptionkeys() {
        return encryptionkeys;
    }

    private Map<String, Object> encryptionkeys;

    public void setEncryptionkeys(Map<String, Object> encryption) {
        this.encryptionkeys = encryption;
    }

    public static synchronized SharedData getInstance(){
        if (sharedInstance == null) {
            synchronized (SharedData.class) {
                if (sharedInstance == null) {
                    sharedInstance = new SharedData();
                }
            }
        }
        return sharedInstance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        if (currentUser != null) {
            this.currentUser = currentUser;
        } else {
            throw new IllegalArgumentException("User data cannot be null");
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        if (activities != null) {
            this.activities = activities;
        } else {
            throw new IllegalArgumentException("Activity data cannot be null");
        }
    }
    public void addActivity(Activity activity) {
        if (activities != null) {
            this.activities.add(activity);
        } else {
            throw new IllegalArgumentException("Activity data cannot be null");
        }
    }

    public List<Nutrition> getNutritions() {
        return nutritions;
    }

    public void setNutritions(List<Nutrition> Nutritions) {
        if (Nutritions != null) {
            this.nutritions = nutritions;
        } else {
            throw new IllegalArgumentException("Nutrition data is invalid");
        }
    }

    public void addNutrition(Nutrition nutrition) {
        if (nutrition != null) {
            this.nutritions.add(nutrition);
        } else {
            throw new IllegalArgumentException("Nutrition data cannot be null");
        }
    }

    public String getWorkouts() {
        return workouts;
    }

    public void setWorkouts(String workouts) {
        this.workouts = workouts;
    }

    public LocalDateTime getWorkoutTime() {
        return workoutTime;
    }

    public void setWorkoutTime(LocalDateTime date){
        this.workoutTime=date;
    }
}