package com.group21.NutriFit.database.File;

import com.group21.NutriFit.Model.Profile;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.database.Database;
import com.group21.NutriFit.utils.FileQueueProcessor;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.group21.NutriFit.utils.Encryption.*;


public class UserDB implements Database<User> {
    private FileQueueProcessor fileProcessor = new FileQueueProcessor();
    private String filePath = "./users.dat";

    public UserDB(){
        init(filePath);
    }

    @Override
    public boolean add(User item) {
        int userID = item.getUserID();
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        if (data.get(1).contains(String.valueOf(userID))) {
            LOGGER.log(Level.WARNING, "User with userID "+userID+" already exists!");
            return false;
        }
        if (!data.get(1).isEmpty()) {
            data.set(1, data.get(1) + ",");
        }
        data.set(1, data.get(1) + userID);
        try {
            data.add(encrypt(item.getPublic(), item.toString()));

            writeToFile(filePath, String.join("\n", data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean remove(User item) {
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        if (data.size() < 2) return false;

        int userID = item.getUserID();
        int userIndex = -1;
        List<String> userIDs = new ArrayList<>(List.of(data.get(1).split(",")));

        for (int i = 0; i < userIDs.size(); i++) {
            System.out.println(userIDs.get(i));
            if (userIDs.get(i).equals(String.valueOf(userID))) {
                userIndex = i;
                break;
            }
        }
        if (userIndex == -1) {
            return false;
        }

        userIDs.remove(userIndex);
        data.set(1, String.join(",", userIDs));
        data.remove(userIndex+3);
        writeToFile(filePath, String.join("\n", data));
        return true;
    }

    public String getByID(int userID, PrivateKey privateKey){
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        if (data.size() < 2) return null;

        int userIndex = -1;
        List<String> userIDs = new ArrayList<>(List.of(data.get(1).split(",")));

        for (int i = 0; i < userIDs.size(); i++) {
            System.out.println(userIDs.get(i));
            if (userIDs.get(i).equals(String.valueOf(userID))) {
                userIndex = i;
                break;
            }
        }
        if (userIndex == -1) {
            return null;
        }

        data.set(1, String.join(",", userIDs));
        try {
            return decrypt(privateKey, data.get(userIndex + 3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        UserDB userDB = new UserDB();
        User newUser = new User(1, "ad", "ad.go", 123, 123, 100, 'M', new Profile());
        User newUser1 = new User(2, "ad", "ad.go", 123, 123, 100, 'M', new Profile());
//        User newUser2 = new User(3, "ad", "ad.go", 123, 123, 100, 'M', new Profile());
        userDB.add(newUser);
        userDB.add(newUser1);
        System.out.println(String.join("\n", userDB.fileProcessor.readFile(userDB.filePath)));
    }
}
