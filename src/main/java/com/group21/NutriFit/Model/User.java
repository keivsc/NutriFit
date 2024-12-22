package com.group21.NutriFit.Model;

import com.group21.NutriFit.utils.Database;
import com.group21.NutriFit.utils.Encryption;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class User extends BaseModel<User> {
    private final String filePath = "./data/users.dat";
    private String name;
    private String email;
    private int age;
    private double height;
    private double weight;
    private char sex;
    private byte[] profilePic;
    private boolean isPublic;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private double weightGoal; // Target weight
    private int intensity;     // Exercise intensity (1: low, 2: medium, 3: high)

    public User(int userID, String name, String email, int age, double height, double weight, char sex, boolean isPublic, byte[] profilePic, double weightGoal, int intensity) {
        super(userID);
        this.name = name;
        this.email = email;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.profilePic = profilePic;
        this.isPublic = isPublic;
        this.weightGoal = weightGoal;
        this.intensity = intensity;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
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

    public double getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(double weightGoal) {
        this.weightGoal = weightGoal;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "User{" +
                userID +
                ", " + name +
                ", " + email +
                ", " + age +
                ", " + height +
                ", " + weight +
                ", " + sex +
                ", " + isPublic +
                ", " + weightGoal +
                ", " + intensity +
                ", " + (profilePic != null ? Arrays.toString(profilePic) : "null") +
                '}';
    }

    public static User fromString(String string) {
        try {
            // Clean the input string to remove the 'User{' prefix and '}' suffix
            String cleanedString = string.replace("User{", "").replace("}", "").trim();

            // Split the string by ", " to separate each attribute
            String[] data = cleanedString.split(", ");

            // Ensure the string has the correct number of elements
            if (data.length < 10) { // Adjust for weightGoal and intensity
                throw new IllegalArgumentException("Insufficient data to parse User: " + string);
            }

            // Parse values from the string array
            int userID = Integer.parseInt(data[0].trim());
            String name = data[1].trim();
            String email = data[2].trim();
            int age = Integer.parseInt(data[3].trim());
            double height = Double.parseDouble(data[4].trim());
            double weight = Double.parseDouble(data[5].trim());
            char sex = data[6].trim().charAt(0);
            boolean isPublic = Boolean.parseBoolean(data[7].trim());
            double weightGoal = Double.parseDouble(data[8].trim());
            int intensity = Integer.parseInt(data[9].trim());

            byte[] profilePic = null;
            if (!"null".equals(data[10].trim())) {
                String profilePicString = data[10].trim();
                // Remove the square brackets and split by comma
                String[] byteStrings = profilePicString.substring(1, profilePicString.length() - 1).split(", ");
                profilePic = new byte[byteStrings.length];
                for (int i = 0; i < byteStrings.length; i++) {
                    profilePic[i] = Byte.parseByte(byteStrings[i].trim());
                }
            }

            // Return a new User object using the parsed values
            return new User(userID, name, email, age, height, weight, sex, isPublic, profilePic, weightGoal, intensity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in input string: " + string, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing fields in input string: " + string, e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing User from string: " + string, e);
        }
    }

    public static Map<String, Object> authorise(String email, String password) {
        Database<String> keysDB = new Database<>("keys.dat");
        Database<String> userDB = new Database<>("users.dat");
        try {
            Map<String, Object> allData = new HashMap<>();
            Map<String, Object> keys = new HashMap<>(Objects.requireNonNull(Encryption.parseKeys(keysDB.get(email), email, password)));
            User userData = User.fromString(Encryption.decrypt((PrivateKey) keys.get("PrivateKey"), userDB.get(email)));
            allData.put("Keys", keys);
            allData.put("User", userData);
            return allData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int newUser(String email, String password, String name, LocalDate dob) {
        Database<String> db = new Database<>("keys.dat");
        if (db.get(email) != null) {
            return 1;
        }
        try {
            PublicKey publicKey = Encryption.generateKeys(email, password);
            Database<User> userDB = new Database<>("users.dat");
            int userID = userDB.getNewID();
            userDB.add(email, new User(userID, name, email, Period.between(dob, LocalDate.now()).getYears(), 0, 0, 'N', false, null, 0, 0));
            userDB.pushDataEncrypted(publicKey);
            return 0;
        } catch (Exception e) {
            Logger.getLogger(User.class.getName()).warning("Unable to create account");
            throw new RuntimeException(e);
        }
    }
}
