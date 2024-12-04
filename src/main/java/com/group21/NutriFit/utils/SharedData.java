package com.group21.NutriFit.utils;

import com.group21.NutriFit.Model.Activity;
import com.group21.NutriFit.Model.Goal;
import com.group21.NutriFit.Model.Profile;
import com.group21.NutriFit.Model.User;

import java.util.List;

public class SharedData {
    private static SharedData sharedInstance;
    private User currentUser = null;
    private List<Activity> activities = null;
    private List<Goal> goals = null;
    private Profile userProfile = null;

    public SharedData(){

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

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        if (goals != null) {
            this.goals = goals;
        } else {
            throw new IllegalArgumentException("Goal data is invalid");
        }
    }

    public Profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Profile userProfile) {
        if (userProfile != null) {
            this.userProfile = userProfile;
        } else {
            throw new IllegalArgumentException("Profile data cannot be null");
        }
    }
}