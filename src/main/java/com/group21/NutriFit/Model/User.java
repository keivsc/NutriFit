package com.group21.NutriFit.Model;

import com.group21.NutriFit.utils.Encryption;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class User {
    private int userID;
    private String name;
    private String email;
    private int pNo;
    private int height;
    private int weight;
    private char sex;
    private Profile profile;
    private PublicKey publicKey;
    private PrivateKey privateKey;



    public User(int userID, String name, String email, int pNo, int height, int weight, char sex, Profile profile){
        setEmail(email);
        setHeight(height);
        setName(name);
        setpNo(pNo);
        setWeight(weight);
        setProfile(profile);
        setUserID(userID);
    }

    public User(String userData) {
        userData = userData.substring(1, userData.length() - 1).trim();
        String[] lines = userData.split(",\n");


        for (String line : lines) {
            String[] keyValue = line.split(": ", 2);
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim().replace("\"", "");

            switch (key) {
                case "userID":
                    this.userID = Integer.parseInt(value);
                    break;
                case "name":
                    this.name = value;
                    break;
                case "email":
                    this.email = value;
                    break;
                case "pNo":
                    this.pNo = Integer.parseInt(value);
                    break;
                case "height":
                    this.height = Integer.parseInt(value);
                    break;
                case "weight":
                    this.weight = Integer.parseInt(value);
                    break;
                case "sex":
                    this.sex = value.charAt(0);
                    break;
//                case "profile":
//                    this.profile = new Profile(value); // Profile parsing needs to be handled
//                    break;
                case "publicKey":
                    if (!value.equals("null")) {
                        try {
                            this.publicKey = Encryption.stringToPublic(value);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Error parsing public key", e);
                        }
                    }
                    break;
            }
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
        return publicKey;
    }

    public PrivateKey getPrivate(){
        return privateKey;
    }

    public void setPublic(PublicKey publicKey){
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        // Format the string in a structured way
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pNo=" + pNo +
                ", height=" + height +
                ", weight=" + weight +
                ", sex=" + sex +
                ", profile=" + profile + // Assuming profile has a sensible toString() method
                ", publicKey=" + (publicKey != null ? publicKey.toString() : "null") +
                '}';
    }
}
