package com.group21.NutriFit.database;


import com.group21.NutriFit.utils.FileProcessor;

import java.io.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.group21.NutriFit.utils.Encryption.encrypt;
import static com.group21.NutriFit.utils.Utils.*;

public abstract interface Database<T> {
    static final FileProcessor fileProcessor = new FileProcessor();
    static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    static final String baseFormat = "[userID]\n\n[data]";
    static String filePath = "";

    default void init(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();

            if (firstLine == null || !base64Decode(firstLine).startsWith("[userID]")) {
                LOGGER.log(Level.WARNING, "File " + filePath + " is invalid or does not start with [userID]. Recreating file.");
                overrideFile(filePath, base64Encode(baseFormat));
            }

            LOGGER.log(Level.INFO, filePath + " initialised successfully.");

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "File " + filePath + " not found.");
            LOGGER.log(Level.INFO, "Creating new " + filePath + " file.");
            fileProcessor.createFile(filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to read " + filePath + ": " + Arrays.toString(e.getStackTrace()));
        } catch(IllegalArgumentException e){
            LOGGER.log(Level.SEVERE, "Broken "+filePath+ ": " + Arrays.toString(e.getStackTrace()));
            LOGGER.log(Level.INFO, "Resetting "+filePath);
            try {
                overrideFile(filePath, base64Encode(baseFormat));
            }catch(IOException ex){
                LOGGER.log(Level.SEVERE, "Unable to rewrite "+filePath);
                throw new RuntimeException(ex);
            }
        }
    }


    default boolean add(int ID, T item, PublicKey publicKey) {
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        if (data.get(1).contains(String.valueOf(ID))) {
            LOGGER.log(Level.WARNING, "Item with ID "+ID+" already exists!");
            return false;
        }
        if (!data.get(1).isEmpty()) {
            data.set(1, data.get(1) + ",");
        }
        data.set(1, data.get(1) + ID);
        try {
            data.add(encrypt(publicKey, item.toString()));

            writeToFile(filePath, String.join("\n", data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    default boolean remove(int ID) {
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        if (data.size() < 2) return false;

        int itemIndex = -1;
        List<String> IDs = new ArrayList<>(List.of(data.get(1).split(",")));

        for (int i = 0; i < IDs.size(); i++) {
            System.out.println(IDs.get(i));
            if (IDs.get(i).equals(String.valueOf(ID))) {
                itemIndex = i;
                break;
            }
        }
        if (itemIndex == -1) {
            return false;
        }

        IDs.remove(itemIndex);
        data.set(1, String.join(",", IDs));
        data.remove(itemIndex+3);
        writeToFile(filePath, String.join("\n", data));
        return true;
    }

    default void writeToFile(String filePath, String data){
        fileProcessor.writeFileAsync(filePath, base64Encode(data));
    }
}
