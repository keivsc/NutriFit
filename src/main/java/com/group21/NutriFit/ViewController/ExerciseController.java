package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.Activity;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.Database;
import com.group21.NutriFit.utils.SharedData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;

import java.security.PublicKey;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ExerciseController extends DefaultController{
    @FXML
    private Text username;
    @FXML
    private Button pullButton;
    @FXML
    private Label pullTimer;
    @FXML
    private Button pushButton;
    @FXML
    private Label pushTimer;
    @FXML
    private Button legButton;
    @FXML
    private Label legTimer;

    private SharedData sharedData;

    private Thread workoutTimerThread;

    @FXML
    private void initialize() {
        // Set the username and progress bars
        primaryStage.getScene().getWindow().setOnHidden(event -> stopWorkoutTimerThread());
        sharedData = getSharedData();
        User currentUser = sharedData.getCurrentUser();

        // Check if user information is complete
        if (currentUser.getWeight() == 0 || currentUser.getHeight() == 0 || currentUser.getSex() == 'N') {
            Platform.runLater(() -> {
                showPopup("Please enter your information in the Settings!");
                switchScene("Settings");
            });
            return; // Stop further execution
        }

        username.setText(currentUser.getName());
        pushButton.setOnMouseClicked(event -> onWorkoutButtonClick(event, "push"));
        pullButton.setOnMouseClicked(event -> onWorkoutButtonClick(event, "pull"));
        legButton.setOnMouseClicked(event -> onWorkoutButtonClick(event, "leg"));

        // Get the current workout type (e.g., "pull", "push", or "legs")
        String workout = sharedData.getWorkouts();

        // Retrieve workout timer as LocalDate, assuming the timer is stored in seconds
        // Adjust this based on your actual implementation of 'getWorkoutTimer'
        LocalDateTime workoutTime = sharedData.getWorkoutTime();  // Assuming this returns LocalDate or a string

        // Check the workout type and update the respective button and timer
        startWorkoutThread(workout);
        switch (workout) {
            case "pull":
                pullButton.setText("Stop");
                pullButton.setDisable(false);
                pullTimer.setText(formatTime(workoutTime)); // Format and display the time
                pullTimer.setVisible(true);

                legButton.setDisable(true);
                pushButton.setDisable(true);
                break;
            case "push":
                pushButton.setText("Stop");
                pushButton.setDisable(false);
                pushTimer.setText(formatTime(workoutTime)); // Format and display the time
                pushTimer.setVisible(true);

                pullButton.setDisable(true);
                legButton.setDisable(true);
                break;
            case "leg":
                legButton.setText("Stop");
                legButton.setDisable(false);
                legTimer.setText(formatTime(workoutTime)); // Format and display the time
                legTimer.setVisible(true);

                pullButton.setDisable(true);
                pushButton.setDisable(true);
                break;
            default:
                stopWorkoutTimerThread();
                break;
        }
    }

    private String formatTime(LocalDateTime workoutTime) {
        try {
            // Calculate the duration between the workout start time and the current time
            Duration duration = Duration.between(workoutTime, LocalDateTime.now());

            // Get the total minutes and seconds
            long minutes = duration.toMinutes();
            long seconds = duration.getSeconds() % 60;

            // Return the formatted time as MM:SS
            return String.format("%02d:%02d", minutes, seconds);
        } catch (Exception e) {
            // Handle error if the workoutTime is not valid or parsing fails
            return "00:00";
        }
    }



    @FXML
    protected void onWorkoutButtonClick(MouseEvent event, String workoutType) {
        Database<Activity> activityDB = new Database<>("activities.dat");
        int type = 0;
        String workout = sharedData.getWorkouts();
        LocalDateTime workoutTime = sharedData.getWorkoutTime();
        double calorieBurned = 0;

        // If there is an active workout (i.e., valid workout in sharedData)


        // Handle the workout type and calories if starting a new workout
        switch (workoutType) {
            case "pull":
                type = 1;
                calorieBurned = 7.5;
                break;
            case "push":
                type = 2;
                calorieBurned = 7.5;
                break;
            case "leg":
                type = 3;
                calorieBurned = 8.5;
                break;
        }
        if (!workout.isEmpty()) {
            stopWorkoutTimerThread();
            pullButton.setText("Start");
            pushButton.setText("Start");
            legButton.setText("Start");
            pullButton.setDisable(false);
            pushButton.setDisable(false);
            legButton.setDisable(false);
            pullTimer.setVisible(false);
            pushTimer.setVisible(false);
            legTimer.setVisible(false);
            int duration = (int) Duration.between(sharedData.getWorkoutTime(), LocalDateTime.now()).getSeconds();
            Activity completedWorkout = new Activity(activityDB.getNewID(), sharedData.getCurrentUser().getUserID(), type, duration, calorieBurned*duration, (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            sharedData.addActivity(completedWorkout);
            activityDB.add(sharedData.getCurrentUser().getUserID(), completedWorkout);
            activityDB.pushDataEncrypted((PublicKey) sharedData.getEncryptionkeys().get("PublicKey"));
            sharedData.setWorkouts(""); // Reset the workout
            sharedData.setWorkoutTime(null);
            return; // Exit early to stop further execution
        }
        // Start the workout if no workout is active
        sharedData.setWorkoutTime(LocalDateTime.now());
        sharedData.setWorkouts(workoutType); // Set the workout type in sharedData
        startWorkoutThread(workoutType); // Start the workout timer thread

        // Disable buttons and start timer for the current workout
        switch (workoutType) {
            case "pull":
                pullButton.setText("Stop");
                pullButton.setDisable(false);  // Disable the pull button while workout is active
                pullTimer.setText(formatTime(sharedData.getWorkoutTime()));
                pullTimer.setVisible(true);

                // Disable other workout buttons
                pushButton.setDisable(true);
                legButton.setDisable(true);
                break;
            case "push":
                pushButton.setText("Stop");
                pushButton.setDisable(false);  // Disable the push button while workout is active
                pushTimer.setText(formatTime(sharedData.getWorkoutTime()));
                pushTimer.setVisible(true);

                // Disable other workout buttons
                pullButton.setDisable(true);
                legButton.setDisable(true);
                break;
            case "leg":
                legButton.setText("Stop");
                legButton.setDisable(false);  // Disable the leg button while workout is active
                legTimer.setText(formatTime(sharedData.getWorkoutTime()));
                legTimer.setVisible(true);

                // Disable other workout buttons
                pullButton.setDisable(true);
                pushButton.setDisable(true);
                break;
        }
    }

    private void startWorkoutThread(String workoutType) {
        workoutTimerThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) { // Keep the thread running
                    Thread.sleep(1000); // Wait for 1 second
                    Platform.runLater(() -> { // Ensure UI updates on the JavaFX Application Thread
                        switch (workoutType) {
                            case "pull":
                                pullTimer.setText(formatTime(sharedData.getWorkoutTime())); // Update the pull timer
                                break;
                            case "push":
                                pushTimer.setText(formatTime(sharedData.getWorkoutTime())); // Update the push timer
                                break;
                            case "leg":
                                legTimer.setText(formatTime(sharedData.getWorkoutTime())); // Update the leg timer
                                break;
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        workoutTimerThread.start(); // Start the workout timer thread
    }

    private void stopWorkoutTimerThread() {
        if (workoutTimerThread != null && !workoutTimerThread.isInterrupted()) {
            workoutTimerThread.interrupt(); // Interrupt the workout timer thread
        }
    }





}
