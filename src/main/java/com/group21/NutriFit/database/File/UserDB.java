package com.group21.NutriFit.database.File;

import com.group21.NutriFit.Model.Profile;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.database.Database;
import com.group21.NutriFit.utils.FileProcessor;
import com.group21.NutriFit.utils.Utils;

import static com.group21.NutriFit.utils.Encryption.*;
import static com.group21.NutriFit.utils.Utils.base64Encode;
import static com.group21.NutriFit.utils.Utils.overrideFile;

import java.io.*;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;


public class UserDB implements Database<User> {
    private final FileProcessor fileProcessor = new FileProcessor();
    private final String filePath = "./users.dat";
    private final String keyPath = "./userKeys.dat";

    public UserDB(){
        init(filePath);
        initKeyPath();
    }

    private void initKeyPath(){
        try (BufferedReader reader = new BufferedReader(new FileReader(keyPath))) {
            String firstLine = reader.readLine();

            if (firstLine == null || !Utils.base64Decode(firstLine).startsWith("[email]")) {
                LOGGER.log(Level.WARNING, "File " + keyPath + " is invalid or does not start with [email]. Recreating file.");
                overrideFile(keyPath, base64Encode(baseFormat.replace("userID", "email")));
            }

            LOGGER.log(Level.INFO, keyPath + " initialised successfully.");

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "File " + keyPath + " not found.");
            LOGGER.log(Level.INFO, "Creating new " + keyPath + " file.");
            fileProcessor.createFile(keyPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to read " + keyPath + ": " + Arrays.toString(e.getStackTrace()));
        } catch(IllegalArgumentException e){
            LOGGER.log(Level.SEVERE, "Broken "+keyPath+ ": " + Arrays.toString(e.getStackTrace()));
            LOGGER.log(Level.INFO, "Resetting "+keyPath);
            try {
                overrideFile(filePath, base64Encode(baseFormat.replace("userID", "email")));
            }catch(IOException ex){
                LOGGER.log(Level.SEVERE, "Unable to rewrite "+keyPath);
                throw new RuntimeException(ex);
            }
        }
    }

    public void authorise(String email, String password) {
        List<String> data = new ArrayList<>(fileProcessor.readFile(keyPath));
        if (data.size() < 2) {
            LOGGER.log(Level.WARNING, "No data available for authorization!");
            return;
        }

        int emailIndex = -1;
        List<String> emails = new ArrayList<>(List.of(data.get(1).split(",")));

        // Find the index of the email
        for (int i = 0; i < emails.size(); i++) {
            if (emails.get(i).equals(email)) {
                emailIndex = i;
                break;
            }
        }

        if (emailIndex == -1) {
            LOGGER.log(Level.WARNING, "Email not found for authorization: " + email);
            return;
        }

        try {
            // Decrypt the key associated with the email using the provided password
            String encryptedKey = data.get(emailIndex + 3);
            //PrivateKey decryptedKey = stringToPrivate(decryptKey(email, password, encryptedKey));
//            System.out.println(getByID(1, decryptedKey));
//            LOGGER.log(Level.INFO, "Authorization successful for email: " + email);
//            LOGGER.log(Level.INFO, "Decrypted key: " + decryptedKey);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Authorization failed for email: " + email, e);
            throw new RuntimeException(e);
        }
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
        userDB.add(newUser.getUserID(), newUser, newUser.getPublic());
        userDB.authorise("ad.go", "123123");
        System.out.println(String.join("\n", userDB.fileProcessor.readFile(userDB.filePath)));
    }
}
