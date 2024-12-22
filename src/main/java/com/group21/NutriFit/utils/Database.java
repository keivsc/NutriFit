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
    protected final Map<Object, T> records = new ConcurrentHashMap<>(); // Thread-safe storage for records
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
    public void add(Object id, T record) {
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
        for (Object ID : records.keySet()) {
            data.set(1, data.get(1)+", "+ID);
        }
        for (T dataItem : records.values()) {
            data.add(dataItem.toString());
        }

        fileProcessor.writeFileAsync(filePath, base64Encode(String.join("\n", data)));
    }

    public void pushDataEncrypted(PublicKey publicKey){
        List<String> data = new ArrayList<>(fileProcessor.readFile(filePath));
        for (Object ID : records.keySet()) {
            data.set(1, data.get(1)+", "+ID);
        }
        for (T dataItem : records.values()) {
            try {
                data.add(Encryption.encrypt(publicKey, dataItem.toString()));
            } catch (Exception e) {
                LOGGER.warning("Unable to encrypt data");
            }
        }

        fileProcessor.writeFileAsync(filePath, base64Encode(String.join("\n", data)));
    }

    public T get(Object ID){
        System.out.println(records);
        if(records.isEmpty()){
            renewData(ID);
            return records.get(ID);
        }else{
            return records.get(ID);
        }
    }

    public int getNewID() {
        // Read the file content into a List<String>
        List<String> fileContent = fileProcessor.readFile(filePath);

        // Ensure the file content has enough lines for IDs
        if (fileContent.size() < 2) {
            LOGGER.warning("Insufficient data in file to generate a new ID.");
            return 1; // Return -1 to indicate failure
        }

        // Extract user IDs from the second line
        List<String> IDs = List.of(fileContent.get(1).split(", "));

        // Ensure the IDs list is not empty
        if (IDs.isEmpty()) {
            LOGGER.warning("No IDs found in the file.");
            return 1; // Return 1 as the first ID
        }

        try {
            // Get the last ID in the list and parse it as an integer
            int lastID = Integer.parseInt(IDs.get(IDs.size() - 1));

            // Return the next ID (lastID + 1)
            return lastID + 1;
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid ID format in the last entry: " + IDs.get(IDs.size() - 1));
            return 1; // Return -1 to indicate failure
        }
    }


    public void renewData(Object id) {
        // Read the file content into a List<String> (already decoded)
        List<String> fileContent = fileProcessor.readFile(filePath);

        // Ensure the file content has enough lines
        if (fileContent.size() < 2) {
            LOGGER.warning("Insufficient data in file. Unable to renew data for ID: " + id);
            return;
        }

        // Extract user IDs and data
        List<String> IDs = List.of(fileContent.get(1).split(", "));
        int IDIndex = IDs.indexOf(String.valueOf(id));
        System.out.println(String.valueOf(id));
        System.out.println(IDs);
        if (IDIndex != -1) {
            if (fileContent.size() > IDIndex + 2) {
                System.out.println(id);
                System.out.println((T) fileContent.get(IDIndex + 2));
                records.put(id, (T) fileContent.get(IDIndex + 2));
            } else {
                LOGGER.warning("Data for ID: " + id + " not found in the file.");
            }
        } else {
            LOGGER.warning("ID: " + id + " not found in the ID list.");
        }
    }

    // Log and throw an exception with a message
    protected void logAndThrow(String message, Exception e) {   
        LOGGER.log(Level.SEVERE, message, e);
        throw new RuntimeException(message, e);
    }
}
