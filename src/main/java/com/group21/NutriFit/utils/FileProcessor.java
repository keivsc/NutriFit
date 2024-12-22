package com.group21.NutriFit.utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static com.group21.NutriFit.utils.Utils.base64Decode;
import static com.group21.NutriFit.utils.Utils.base64Encode;

public class FileProcessor {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor(); // Executor for async operations
    private static final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(); // Queue for write tasks
    private static final String dataPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;

    // Constructor initializes a background thread that processes the queued tasks
    public FileProcessor() {
        // Start a background thread to process tasks in the queue
        executor.submit(() -> {
            while (true) {
                try {
                    Runnable task = taskQueue.take();  // Blocks until there is a task to process
                    task.run(); // Execute the task
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    break;
                }
            }
        });
    }

    public void createFile(String filePath, Boolean override) {
        try {
            if (!override) {
                filePath = dataPath +File.separator+ filePath;
                System.out.println(filePath);
                File dir = new File(dataPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created: " + filePath);
                writeFile(filePath, base64Encode("[id]\n\n[data]"));
                System.out.println("Written to: "+filePath);
            } else {
                System.out.println("File already exists or could not be created: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Synchronously read the file
    public List<String> readFile(String filePath) {
        filePath = dataPath +File.separator+ filePath;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder data = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            String decodedData = Utils.base64Decode(data.toString());
            System.out.println(decodedData);
            return List.of(decodedData.split("\n"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: " + filePath, e);
        }
    }

    // Queued asynchronous write to file
    public void writeFileAsync(String filePath, String data) {
        // Create a task to write data to the file
        String fullFilePath = dataPath +File.separator+ filePath; // Compute full file path before the lambda

        Runnable writeTask = () -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilePath))) {
                writer.write(data);
                System.out.println("Data written to file: " + fullFilePath);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        };

        // Add the task to the queue
        try {
            taskQueue.put(writeTask); // This will block if the queue is full
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.err.println("Error queuing the task: " + e.getMessage());
        }
    }


    public void writeFile(String filePath, String data){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
            writer.flush();
            System.out.println("Data written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public FileData parseData(String data){
        List<String> splitData = List.of(data.split("\\[.*?\\]"));
        return new FileData(splitData);
    }

    // Optional: Shutdown executor after all tasks are done (if needed)
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public static class FileData extends ArrayList<String>{
        private List<String> identifier = new ArrayList<>();
        private List<String> data = new ArrayList<>();


        public FileData(List<String> data){
            super(data);
            this.identifier = List.of(data.getFirst().split(", "));
            this.data = List.of(data.getLast().split("\n"));
        }

        public List<String> getAllData() {
            return this.data;
        }

        public List<String> getAllID(){
            return this.identifier;
        }

        public String getDataByID(String id){
            return this.data.get(identifier.indexOf(id));
        }

        public boolean checkID(String id){
            return this.identifier.contains(id);
        }
    }

    public static void main(String[] args){
        FileData fileData = new FileData(List.of(new String[]{"1, 2, 3", "a\nb\nc"}));
        System.out.println(fileData.getFirst());
        System.out.println(fileData.getDataByID("1"));
    }


}