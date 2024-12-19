package com.group21.NutriFit.Model;

import java.security.PublicKey;
import java.security.PrivateKey;

public class User extends BaseModel<User> {
    private String name;
    private String email;
    private int pNo;
    private int height;
    private int weight;
    private char sex;
    private byte[] profilePic;
    private boolean isPublic;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public User(int userID, String name, String email, int pNo, int height, int weight, char sex, byte[] profilePic, boolean isPublic) {
        super(userID);
        this.name = name;
        this.email = email;
        this.pNo = pNo;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.profilePic = profilePic;
        this.isPublic = isPublic;
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

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "User{" +
                userID +
                ", " + name +
                ", " + email +
                ", " + pNo +
                ", " + height +
                ", " + weight +
                ", " + sex +
                ", " + isPublic +
                ", " + (profilePic != null ? profilePic.toString() : "null") +
                ", " + (publicKey != null ? publicKey.toString() : "null") +
                '}';
    }
    public static User fromString(String string) {
        try {
            // Clean and split input string
            String cleanedString = string.replace("User{", "").replace("}", "").trim();
            String[] data = cleanedString.split(", ");

            // Parse values and construct the User object
            int userID = Integer.parseInt(data[0].trim());
            String name = data[1].trim();
            String email = data[2].trim();
            int pNo = Integer.parseInt(data[3].trim());
            int height = Integer.parseInt(data[4].trim());
            int weight = Integer.parseInt(data[5].trim());
            char sex = data[6].trim().charAt(0);
            boolean isPublic = Boolean.parseBoolean(data[7].trim());

            // Assuming profilePic is optional and can be null
            byte[] profilePic = null;
            if (!data[8].equals("null")) {
                profilePic = data[8].trim().getBytes();
            }

            return new User(userID, name, email, pNo, height, weight, sex, profilePic, isPublic);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in input string: " + string, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing fields in input string: " + string, e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing User from string: " + string, e);
        }
    }
}
