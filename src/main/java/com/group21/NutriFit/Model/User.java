package com.group21.NutriFit.Model;

public class User {
    private int userID;
    private String name;
    private String email;
    private String pNo;
    private int height;
    private int weight;
    private int sex;
    private Profile profile;

    public User(int userID, String name, String email, String pNo, int height, int weight, int sex, Profile profile){
        setEmail(email);
        setHeight(height);
        setName(name);
        setpNo(pNo);
        setWeight(weight);
        setProfile(profile);
        setUserID(userID);
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getpNo() {
        return pNo;
    }

    public void setpNo(String pNo) {
        this.pNo = pNo;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
