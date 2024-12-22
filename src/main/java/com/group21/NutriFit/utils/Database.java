package com.group21.NutriFit.utils;

import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.group21.NutriFit.utils.Utils.base64Decode;
import static com.group21.NutriFit.utils.Utils.base64Encode;

public class Database<T> {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    protected final FileProcessor fileProcessor = new FileProcessor();
    protected final Map<Integer, T> records = new ConcurrentHashMap<>(); // Thread-safe storage for records
    protected final String filePath;

    public Database(String filePath) {
        this.filePath = filePath;
        initializeFile(filePath);
    }

    // Initialize the database file
    protected void initializeFile(String filePath) {
        fileProcessor.createFile(filePath, false);
        LOGGER.info("Database file initialized: " + filePath);
    }

    // Add a record to the database
    public void add(int id, T record) {
        if (records.putIfAbsent(id, record) == null) {
            LOGGER.info("Record added with ID: " + id);
        } else {
            LOGGER.warning("Record with ID " + id + " already exists. Use update() to modify it.");
        }
    }


    // Update an existing record
    public void update(int id, T updatedRecord) {
        if (records.replace(id, updatedRecord) != null) {
            LOGGER.info("Record updated with ID: " + id);
        } else {
            LOGGER.warning("No record found with ID: " + id);
        }
    }

    // Remove a record by its ID
    public void remove(int id) {
        if (records.remove(id) != null) {
            LOGGER.info("Record removed with ID: " + id);
        } else {
            LOGGER.warning("No record found with ID: " + id);
        }
    }

    // List all records in the database
    public List<T> listAll() {
        return new ArrayList<>(records.values());
    }

    // Clear all records from the database
    public void clear() {
        records.clear();
        LOGGER.info("All records cleared from the database.");
    }

    public void pushData(){
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        for (Integer ID : records.keySet()) {
            data.set(1, data.get(1)+", "+ID);
        }
        for (T dataItem : records.values()) {
            data.set(data.size()-1, dataItem.toString());
        }

        fileProcessor.writeFileAsync(filePath, base64Encode(String.join("\n", data)));
    }

    public void pushDataEncrypted(PublicKey publicKey){
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        for (Integer ID : records.keySet()) {
            data.set(1, data.get(1)+", "+ID);
        }
        for (T dataItem : records.values()) {
            try {
                data.set(data.size()-1, Encryption.encrypt(publicKey, dataItem.toString()));
            } catch (Exception e) {
                LOGGER.warning("Unable to encrypt data");
            }
        }

        fileProcessor.writeFileAsync(filePath, base64Encode(String.join("\n", data)));
    }

    public T get(int ID){
        if(records.isEmpty()){
            renewData(ID);
        }else{
            return records.get(ID);
        }
        return null;
    }


    public void renewData(int id) {
        // Read the file content into a List<String> (already decoded)
        List<String> fileContent = fileProcessor.readFile(filePath);

        // Extract user IDs and data
        List<String> IDs = List.of(fileContent.get(1).split(", ")); // User IDs are on the second line
        int IDIndex = IDs.indexOf(String.valueOf(id));
        if(IDIndex!=-1){
            records.put(id, (T) fileContent.get(IDIndex+2));
        }
        
    }

    // Log and throw an exception with a message
    protected void logAndThrow(String message, Exception e) {   
        LOGGER.log(Level.SEVERE, message, e);
        throw new RuntimeException(message, e);
    }
}
