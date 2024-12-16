package com.group21.NutriFit.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.group21.NutriFit.utils.Utils.base64Decode;
import static com.group21.NutriFit.utils.Utils.base64Encode;

public abstract class Database<T> {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    protected final FileProcessor fileProcessor = new FileProcessor();
    protected final Map<Integer, T> records = new ConcurrentHashMap<>(); // Thread-safe storage for records
    protected final String filePath;

    protected Database(String filePath) {
        this.filePath = filePath;
        initializeFile(filePath);
    }

    // Initialize the database file
    protected void initializeFile(String filePath) {
        // Implementation for file initialization if needed
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

    // Retrieve a record by its ID
    public T get(int id) {
        return records.get(id);
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
        StringBuilder formattedData = new StringBuilder();
        formattedData.append("[userID]\n");
        for (Integer userID : records.keySet()) {
            formattedData.append(userID).append(" ");
        }
        formattedData.append("\n");
        formattedData.append("[data]\n");
        for (T data : records.values()) {
            formattedData.append(data.toString()).append("\n");
        }
        String result = formattedData.toString();

        fileProcessor.writeFileAsync(filePath, base64Encode(result));
    }

    public void renewData() {
        // Read the file content into a List<String> (already decoded)
        List<String> fileContent = fileProcessor.readFile(filePath);

        // Extract user IDs and data
        String[] userIDs = fileContent.get(1).split(", "); // User IDs are on the second line
        List<T> dataList = (List<T>) new ArrayList<>(fileContent.subList(3, fileContent.size())); // Data starts from the fourth line

        // Ensure userIDs and dataList have the same size
        if (userIDs.length == dataList.size()) {
            for (int i = 0; i < userIDs.length; i++) {
                records.put(Integer.parseInt(userIDs[i]), dataList.get(i));  // Assuming T is String
            }
            LOGGER.info("Data has been successfully renewed.");
        } else {
            LOGGER.warning("Mismatch between user IDs and data.");
        }
    }

    // Log and throw an exception with a message
    protected void logAndThrow(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
        throw new RuntimeException(message, e);
    }
}