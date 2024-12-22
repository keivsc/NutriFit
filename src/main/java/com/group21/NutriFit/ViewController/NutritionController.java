package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.Nutrition;
import com.group21.NutriFit.Model.User;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class NutritionController extends DefaultController {

    @FXML
    private Text username;
    @FXML
    private ProgressBar carbsProgress;
    @FXML
    private Label proteinTarget;
    @FXML
    private Label carbsTarget;
    @FXML
    private Label fatsTarget;
    @FXML
    private ProgressBar proteinProgress;
    @FXML
    private ProgressBar fatsProgress;
    @FXML
    private Arc progressArc;
    @FXML
    private Text progressText;

    @FXML
    private void initialize() {
        // Set the username and progress bars
        User currentUser = getSharedData().getCurrentUser();
        username.setText(currentUser.getName());
        if (currentUser.getWeight() == 0 || currentUser.getHeight() == 0 || currentUser.getSex() == 'N') {
            switchScene("Settings");
            showPopup("Please enter your information in the Settings!");
        }
        updateSharedData();
        double proteinIntake = 0;
        double fatsIntake = 0;
        double carbsIntake = 0;
        for (Nutrition nutrition : getSharedData().getNutritions()) {
            proteinIntake+=nutrition.getProtein();
            fatsIntake+=nutrition.getFat();
            carbsIntake+=nutrition.getCarbs();
        }

        double totalCalories = getSharedData().getCalorieIntake();
        double calorieTarget = getSharedData().getCalorieTarget();

        double proteinTargetPercentage = 0.20; // 20% protein
        double fatsTargetPercentage = 0.30;   // 30% fats
        double carbsTargetPercentage = 0.50;  // 50% carbs

        double proteinTargetGrams = (proteinTargetPercentage * calorieTarget) / 4;
        double fatsTargetGrams = (fatsTargetPercentage * calorieTarget) / 9;
        double carbsTargetGrams = (carbsTargetPercentage * calorieTarget) / 4;

        proteinProgress.setProgress(proteinIntake / proteinTargetGrams);
        fatsProgress.setProgress(fatsIntake / fatsTargetGrams);
        carbsProgress.setProgress(carbsIntake / carbsTargetGrams);

        proteinTarget.setText(String.format("%.1fg / %.1fg", proteinIntake, proteinTargetGrams));
        fatsTarget.setText(String.format("%.1fg / %.1fg", fatsIntake, fatsTargetGrams));
        carbsTarget.setText(String.format("%.1fg / %.1fg", carbsIntake, carbsTargetGrams));


        double totalProgress = (getSharedData().getCalorieIntake()/ getSharedData().getCalorieTarget());

        // Update the arc with the combined progress
        updateProgress(totalProgress);

        // Check if user needs to update profile details
        boolean needsUpdate = false;
        if (currentUser.getWeight() == 0 || currentUser.getHeight() == 0 || currentUser.getSex() == 'N') {
            needsUpdate = true;
        }

        if (needsUpdate) {
            // Navigate to update page or show a prompt if needed
            // Example: navigateToUpdatePage();
        }
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
