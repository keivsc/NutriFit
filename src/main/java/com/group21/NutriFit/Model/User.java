package com.group21.NutriFit.Model;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static com.group21.NutriFit.utils.Encryption.generateKeyPair;

public class User {
    private int userID;
    private String name;
    private String email;
    private int pNo;
    private int height;
    private int weight;
    private char sex;
    private Profile profile;
    private KeyPair keyPair;



    public User(int userID, String name, String email, int pNo, int height, int weight, char sex, Profile profile){
        setEmail(email);
        setHeight(height);
        setName(name);
        setpNo(pNo);
        setWeight(weight);
        setProfile(profile);
        setUserID(userID);
        try {
            this.keyPair = generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public int getpNo() {
        return pNo;
    }

    public void setpNo(int pNo) {
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

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public PublicKey getPublic() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivate(){
        return keyPair.getPrivate();
    }
}
