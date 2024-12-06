package com.group21.NutriFit.database;


import com.group21.NutriFit.utils.FileQueueProcessor;
import com.group21.NutriFit.utils.Utils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.group21.NutriFit.utils.Utils.*;

public abstract interface Database<T> {
    static final FileQueueProcessor fileProcessor = new FileQueueProcessor();
    static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    static final String baseFormat = "[userID]\n\n[data]";

    default void init(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();

            if (firstLine == null || !Utils.base64Decode(firstLine).startsWith("[userID]")) {
                LOGGER.log(Level.WARNING, "File " + filePath + " is invalid or does not start with [userID]. Recreating file.");
                overrideFile(filePath, base64Encode(baseFormat));
            }

            LOGGER.log(Level.INFO, filePath + " initialised successfully.");

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "File " + filePath + " not found.");
            LOGGER.log(Level.INFO, "Creating new " + filePath + " file.");

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

    default List<String> readAllLines(String filePath) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            StringBuilder data= new StringBuilder();
            while((line = reader.readLine()) != null){
                data.append(line);
            }
            String decodedData = base64Decode(data.toString());
            return List.of(decodedData.split("\n"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract boolean add(T item);
    public abstract boolean remove(T item);
    default void writeToFile(String filePath, String data){
        fileProcessor.writeFileAsync(filePath, base64Encode(data));
    }
}
