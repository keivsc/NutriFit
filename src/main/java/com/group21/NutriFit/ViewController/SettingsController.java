package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.security.PublicKey;

public class SettingsController extends DefaultController {
    @FXML
    private Text username;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField weightGoalField;  // New field for weight goal
    @FXML
    private Slider intensitySlider;    // New slider for intensity
    @FXML
    private Button maleButton;
    @FXML
    private Button femaleButton;
    @FXML
    private Button changeButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;

    @FXML
    protected void initialize() {
        User currentUser = getSharedData().getCurrentUser();
        username.setText(currentUser.getName());
        nameField.setText(currentUser.getName());
        ageField.setText(String.valueOf(currentUser.getAge()));
        weightField.setText(String.valueOf(currentUser.getWeight()));
        heightField.setText(String.valueOf(currentUser.getHeight()));
        weightGoalField.setText(String.valueOf(currentUser.getWeightGoal())); // Initialize weight goal

        // Set intensity slider to user's current intensity
        intensitySlider.setValue(currentUser.getIntensity());

        // Highlight the selected sex button
        if (currentUser.getSex() == 'M') {
            maleButton.setStyle("-fx-background-color: #2444A5");
        } else if (currentUser.getSex() == 'F') {
            femaleButton.setStyle("-fx-background-color: #2444A5");
        }
    }

    @FXML
    protected void onChangeClick() {
        nameField.setDisable(false);
        ageField.setDisable(false);
        weightField.setDisable(false);
        heightField.setDisable(false);
        weightGoalField.setDisable(false);  // Enable weight goal field
        intensitySlider.setDisable(false); // Enable intensity slider
        maleButton.setDisable(false);
        femaleButton.setDisable(false);
        changeButton.setVisible(false);
        updateButton.setVisible(true);
        cancelButton.setVisible(true);
    }

    @FXML
    protected void onUpdateClick() {
        // Get the current user
        User currentUser = getSharedData().getCurrentUser();

        // Get the new values from the fields
        String updatedName = nameField.getText();
        String updatedAge = ageField.getText();
        String updatedWeight = weightField.getText();
        String updatedHeight = heightField.getText();
        String updatedWeightGoal = weightGoalField.getText();  // Get weight goal
        int updatedIntensity = (int) intensitySlider.getValue();  // Get intensity value

        // Validate the inputs (ensure they are not empty and that weight/height are numbers)
        if (updatedName.isEmpty() || updatedWeight.isEmpty() || updatedHeight.isEmpty() || updatedWeightGoal.isEmpty()) {
            showPopup("Please fill in all fields!");
            return;
        }

        double weight, height, weightGoal;
        int age;

        try {
            age = Integer.parseInt(updatedAge);
            weight = Double.parseDouble(updatedWeight);
            height = Double.parseDouble(updatedHeight);
            weightGoal = Double.parseDouble(updatedWeightGoal);  // Parse weight goal
        } catch (NumberFormatException e) {
            showPopup("Invalid age, weight, height, or weight goal!");
            return;
        }

        // Validate sex button selection
        char sex = 'N'; // 'N' represents unassigned sex
        if (!maleButton.isDisable() && maleButton.getStyle().equals("-fx-background-color: #2444A5")) {
            sex = 'M';
        } else if (!femaleButton.isDisable() && femaleButton.getStyle().equals("-fx-background-color: #2444A5")) {
            sex = 'F';
        }

        // Update the user model with the new values
        currentUser.setName(updatedName);
        currentUser.setAge(age);
        currentUser.setWeight(weight);
        currentUser.setHeight(height);
        currentUser.setSex(sex);
        currentUser.setWeightGoal(weightGoal);
        currentUser.setIntensity(updatedIntensity);

        Database<User> userDB = new Database<>("users.dat");
        userDB.add(currentUser.getEmail(), currentUser);
        userDB.pushDataEncrypted((PublicKey) getSharedData().getEncryptionkeys().get("PublicKey"));

        showPopup("User Updated!");

        // After successful update, disable editing again and switch back to the original state
        nameField.setDisable(true);
        ageField.setDisable(true);
        weightField.setDisable(true);
        heightField.setDisable(true);
        weightGoalField.setDisable(true);  // Disable weight goal field
        intensitySlider.setDisable(true); // Disable intensity slider
        maleButton.setDisable(true);
        femaleButton.setDisable(true);

        changeButton.setVisible(true);
        updateButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    @FXML
    protected void onCancelClick() {
        switchScene("Settings");
    }

    public void maleButtonClick() {
        if (femaleButton.getStyle().equals("-fx-background-color: #2444A5")) {
            femaleButton.setStyle("-fx-background-color:  #3d5ab3");
        }
        maleButton.setStyle("-fx-background-color: #2444A5");
    }

    public void femaleButtonClick() {
        if (maleButton.getStyle().equals("-fx-background-color: #2444A5")) {
            maleButton.setStyle("-fx-background-color:  #3d5ab3");
        }
        femaleButton.setStyle("-fx-background-color: #2444A5");
    }
}
