package com.group21.NutriFit.utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FileQueueProcessor {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor(); // Executor for async operations
    private static final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(); // Queue for write tasks

    // Constructor initializes a background thread that processes the queued tasks
    public FileQueueProcessor() {
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

    // Synchronously read the file
    public List<String> readFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder data = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            String decodedData = Utils.base64Decode(data.toString());
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
        Runnable writeTask = () -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(data);
                System.out.println("Data written to file: " + filePath);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        };

        // Add the task to the queue
        try {
            taskQueue.put(writeTask);  // This will block if the queue is full
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.err.println("Error queuing the task: " + e.getMessage());
        }
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
}