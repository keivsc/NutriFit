package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.App;
import com.group21.NutriFit.Model.Activity;
import com.group21.NutriFit.Model.Nutrition;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.Database;
import com.group21.NutriFit.utils.Encryption;
import com.group21.NutriFit.utils.SharedData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultController {
    private static SharedData sharedData;
    public static Stage primaryStage;

    static {
        sharedData = SharedData.getInstance();
    }

    public static SharedData getSharedData(){
        return sharedData;
    }

    public void setStage(Stage stage){
        primaryStage = stage;
    }

    public void switchScene(String sceneName)  {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + sceneName + ".fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());

        DefaultController controller = fxmlLoader.getController();
        scene.getStylesheets().add(App.class.getResource("/com/group21/nutrifit/view/src/style.css").toExternalForm());
        controller.setStage(primaryStage);
        primaryStage.setScene(scene);
        }catch(IOException e){
            Logger.getLogger(getClass().getName()).severe("Unable to find specified view");
            throw new RuntimeException(e);
        }
    }

    public void showNavBar(boolean status){
        //primaryStage.getScene().;
    }

    //home controllers
    //sidebar controllers
    //navigation controllers

    /**
     * Validates if the given email matches the standard email regex.
     *
     * @param email The email address to validate.
     * @return True if valid, otherwise false.
     */
    protected boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Resets the style of a TextField. If the TextField is empty, it retains the current style.
     *
     * @param field       The TextField to reset.
     * @param defaultStyle The default CSS style for the field.
     */
    protected void resetFieldStyle(TextField field, String defaultStyle) {
        if (!field.getText().isEmpty()) {
            field.setStyle(defaultStyle);
        }
    }

    /**
     * Resets the style of a DatePicker.
     *
     * @param field The DatePicker to reset.
     */
    protected void resetFieldStyle(DatePicker field) {
        if (field.getValue() != null) {
            field.setStyle(null);
        }
    }

    /**
     * Resets the style of a CheckBox.
     *
     * @param field The CheckBox to reset.
     */
    protected void resetFieldStyle(CheckBox field) {
        if (field.isSelected()) {
            field.setStyle(null);
        }
    }

    protected void addFieldStyle(TextField field, String style){
        field.setStyle(field.getStyle() + style);
    }

    protected void showPopup(String content) {
        AlertType alertType = AlertType.INFORMATION; // Default to INFORMATION

        switch (1) {
            case 1:
                alertType = AlertType.INFORMATION;
                break;
            case 2:
                alertType = AlertType.WARNING;
                break;
            case 3:
                alertType = AlertType.ERROR;
                break;
            case 4:
                alertType = AlertType.CONFIRMATION;
                break;
            default:
                alertType = AlertType.INFORMATION;
        }

        Alert alert = new Alert(alertType);
        alert.setTitle("INFO");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static double calculateCalIntake(double weight, double height, int age, char sex, double targetWeight) {
        double bmr = 0;
        if (sex == 'M') {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else if (sex == 'F') {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        // Calculate the difference in weight and adjust calorie intake accordingly.
        double weightDifference = targetWeight-weight;

        // Assuming 7700 calories are required to lose or gain 1 kg.
        // Positive for weight loss, negative for weight gain.

        return (bmr + (weightDifference * 7700)/30);
    }

    public static double calculateExerciseTime(int intensity, double weight, double targetWeight) {
        // Calculate the weight difference
        double weightDifference = weight - targetWeight;

        // Assuming that 1 kg loss is roughly equivalent to burning 7700 calories
        double caloriesToBurn = weightDifference * 7700;

        if (intensity == 0) {
            throw new IllegalArgumentException("Intensity cannot be zero for exercise calculation.");
        }

        // Assume exercise burns 500 calories per hour at maximum intensity
        double caloriesBurnedPerHour = intensity * 500; // Adjust this as necessary for more realistic estimates

        // Calculate the time required to burn the necessary calories
        return caloriesToBurn / caloriesBurnedPerHour;
    }


    @FXML
    protected void onHomeClick(){
        switchScene("Home");
    }

    @FXML
    protected void onScanFoodClick(){
        switchScene("Scanner");
    }

    @FXML
    protected void onNutritionClick(){
        switchScene("Nutrition");
    }

    @FXML
    protected void onWorkoutClick(){
        switchScene("Exercise");
    }

    @FXML
    protected void onSettingsClick(){
        switchScene("Settings");
    }

    @FXML
    protected void onUserClick() {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to sign out?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                sharedData=new SharedData();
                switchScene("Landing");
                showPopup("You have successfully signed out.");
            }
        });
    }

    protected void updateSharedData(){
        Database<String> activityDB = new Database<>("activities.dat");
        List<String> activities=activityDB.getAll(sharedData.getCurrentUser().getUserID());
        Database<String> nutritionDB = new Database<>("nutrition.dat");
        List<String> nutritions=nutritionDB.getAll(sharedData.getCurrentUser().getUserID());
        System.out.println(nutritions);
        activities.forEach((activity)->{
            
            try {
                if(activity==null){
                    return;
                }
                getSharedData().addActivity(Activity.fromString(Encryption.decrypt((PrivateKey) getSharedData().getEncryptionkeys().get("PrivateKey"), activity)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        nutritions.forEach((nutrition)->{
            
            try {
                getSharedData().addNutrition(Nutrition.fromString(Encryption.decrypt((PrivateKey) getSharedData().getEncryptionkeys().get("PrivateKey"), nutrition)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}

