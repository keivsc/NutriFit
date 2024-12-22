package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;

public class SignUpController extends DefaultController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private CheckBox policyCheck;
    @FXML
    private Text statusText;

    @FXML
    protected void initialize() {
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(firstNameField, null));
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(lastNameField, null));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(emailField, null));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(passwordField, null));
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(confirmPasswordField, null));
        dateField.valueProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(dateField));
        policyCheck.selectedProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(policyCheck));
    }

    @FXML
    protected void onRegisterClick() {
        List<Object> fields = List.of(firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, dateField);
        boolean hasEmptyField = false;

        for (Object field : fields) {
            if (field instanceof TextField) {
                // Reset style first
                resetFieldStyle((TextField) field, null);

                // Validate and apply error style if needed
                if (((TextField) field).getText().isEmpty()) {
                    ((TextField) field).setStyle("-fx-border-color: red;");
                    hasEmptyField = true;
                }
            } else if (field instanceof DatePicker) {
                // Reset style first
                resetFieldStyle((DatePicker) field);

                // Validate and apply error style if needed
                if (((DatePicker) field).getValue() == null) {
                    ((DatePicker) field).setStyle("-fx-border-color: red;");
                    hasEmptyField = true;
                }
            } else if (field instanceof CheckBox) {
                // Reset style first
                resetFieldStyle((CheckBox) field);

                // Validate and apply error style if needed
                if (!((CheckBox) field).isSelected()) {
                    ((CheckBox) field).setStyle("-fx-text-fill: red; -fx-accent: red;");
                    hasEmptyField = true;
                }
            }
        }

        if (hasEmptyField) {
            statusText.setText("Please fill in all fields.");
            return;
        }

        if (!isValidEmail(emailField.getText())) {
            statusText.setText("Invalid email address.");
            emailField.setStyle("-fx-border-color: red;");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            statusText.setText("Passwords do not match.");
            passwordField.setStyle("-fx-border-color: red;");
            confirmPasswordField.setStyle("-fx-border-color: red;");
            return;
        }

        if(!policyCheck.isSelected()){
            statusText.setText("Please agree to the terms and policy.");
            policyCheck.setStyle("-fx-text-fill: red;");
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate dob = dateField.getValue();
        int createStatus = User.newUser(emailField.getText(), passwordField.getText(), firstName+" "+lastName, dob);
        switch(createStatus){
            case 0:
                statusText.setText("Account Created");
                showPopup("Your account has been created at: "+emailField.getText());
                switchScene("Landing");
                break;
            case 1:
                statusText.setText("Email already exists!");
                emailField.setStyle(emailField.getStyle() + "-fx-border-color: red;");
                break;
            case 2:
                statusText.setText("Unable to create account!");
                break;
        }
    }

    @FXML
    protected void onCancelClick() {
        switchScene("Landing");
    }
}