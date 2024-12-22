package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.Activity;
import com.group21.NutriFit.Model.Nutrition;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.SharedData;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HomeController extends DefaultController {

    @FXML
    private Text username;
    @FXML
    private ProgressBar calorieProgress;
    @FXML
    private Label calorieDone;
    @FXML
    private Label calorieTarget;
    @FXML
    private ProgressBar sleepProgress;
    @FXML
    private Label sleepDone;
    @FXML
    private Label sleepTarget;
    @FXML
    private ProgressBar exerciseProgress;
    @FXML
    private Label exerciseDone;
    @FXML
    private Label exerciseTarget;
    @FXML
    private ProgressBar stepProgress;
    @FXML
    private Label stepDone;
    @FXML
    private Label stepTarget;
    @FXML
    private Arc progressArc;
    @FXML
    private Label progressText;

    private SharedData sharedData;

    @FXML
    private void initialize() {
        // Set the username and progress bars
        sharedData = getSharedData();
        User currentUser = sharedData.getCurrentUser();

        if (currentUser.getWeight() == 0 || currentUser.getHeight() == 0 || currentUser.getSex() == 'N') {

            Platform.runLater(() -> {
                showPopup("Please enter your information in the Settings!");
                switchScene("Settings");});
            return; // Stop further execution
        }
        username.setText(currentUser.getName());
        updateSharedData();
        double calorieIntake = 0;
        double calorieGoal = calculateCalIntake(currentUser.getWeight(), currentUser.getHeight(), currentUser.getAge(), currentUser.getSex(), currentUser.getWeightGoal());
        try {
            for (Nutrition nutrition : sharedData.getNutritions()) {
                calorieIntake += nutrition.getCalorieIntake();
            }
            calorieProgress.setProgress(calorieIntake/calorieGoal);
        }catch (Exception ignored){}


        double exerciseDoneSeconds = 0;
        double exerciseGoal = calculateExerciseTime(currentUser.getIntensity(), currentUser.getWeight(), currentUser.getWeightGoal());

        try {
            for (Activity activity : sharedData.getActivities()) {
                exerciseDoneSeconds+=activity.getDuration();
                calorieGoal+=activity.getCaloriesBurned();
                exerciseProgress.setProgress(calorieIntake/calorieGoal);
            }
// Format exercise done time (hours, minutes, seconds)
            exerciseDone.setText(
                    (exerciseDoneSeconds >= 3600 ? (exerciseDoneSeconds / 3600) + "h " : "") +  // Show hours if greater than or equal to 3600 seconds
                            (exerciseDoneSeconds % 3600 / 60 > 0 ? (exerciseDoneSeconds % 3600 / 60) + "m " : "") +  // Show minutes if greater than 0
                            (exerciseDoneSeconds % 60 > 0 || exerciseDoneSeconds < 60 ? (exerciseDoneSeconds % 60) + "s" : "")  // Always show seconds unless 0
            );

// Format exercise goal time (hours, minutes, seconds)
            exerciseTarget.setText(
                    "Target: " +
                            (exerciseGoal >= 3600 ? (int) (exerciseGoal / 3600) + "h " : "") +  // Show hours if greater than or equal to 3600 seconds
                            ((int) ((exerciseGoal % 3600) / 60) > 0 ? (int) ((exerciseGoal % 3600) / 60) + "m " : "") +  // Show minutes if greater than 0
                            (int) (exerciseGoal % 60) + "s"  // Always show seconds
            );

        }catch (Exception ignored){}

        calorieDone.setText(String.format("%.2f Kcal", calorieIntake));
        calorieTarget.setText(String.format("Target: %.2f Kcal", calorieGoal));
        sharedData.setCalorieIntake(calorieIntake);
        sharedData.setCalorieTarget(calorieGoal);
        sleepProgress.setProgress(0.6);
        stepProgress.setProgress(0.8); // Assuming some value for the example

        // Calculate total progress
        double totalProgress = (calorieProgress.getProgress() + sleepProgress.getProgress() +
                exerciseProgress.getProgress() + stepProgress.getProgress()) / 4;

        // Update the arc with the combined progress
        updateProgress(totalProgress);
    }


    private void updateProgress(double progressPercentage) {
        double progressAngle = 360 * progressPercentage;  // Convert progress to angle

        // Ensure the arc stays centered and has a constant radius
        progressArc.setStartAngle(0);  // Start from 0 degrees (or your preferred start angle)
        progressArc.setRadiusX(50);    // Fixed radius value (stays constant)
        progressArc.setRadiusY(50);    // Fixed radius value (stays constant)

        // Append the new progress percentage to the existing text
        progressText.setText(progressText.getText() + String.format("%.0f%%", progressPercentage * 100));

        // Create a KeyValue to animate the arc length (sweep angle)
        KeyValue keyValue = new KeyValue(progressArc.lengthProperty(), progressAngle, Interpolator.EASE_BOTH);

        // Create a KeyFrame to animate the keyValue over 1 second
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        // Create a Timeline to handle the animation of the KeyFrame
        Timeline timeline = new Timeline(keyFrame);

        // Start the animation
        timeline.play();
    }



}
