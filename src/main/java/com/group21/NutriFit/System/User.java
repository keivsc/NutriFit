package com.group21.NutriFit.System;

public class User {
    private int userID;
    private String name;
    private String email;
    private String pNo;
    private int height;
    private int weight;
    private int sex;
    private Profile profile;
    public User(String email){

    }

    public Profile getProfile() {
        return this.profile;
    }
}