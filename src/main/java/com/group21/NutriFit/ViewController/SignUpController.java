package com.group21.NutriFit.ViewController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // Add event listeners to reset styles when content is entered
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(firstNameField));
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(lastNameField));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(emailField));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(passwordField));
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(confirmPasswordField));

        dateField.valueProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(dateField));
        policyCheck.selectedProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(policyCheck));
    }

    private void resetFieldStyle(TextField field) {
        if (!field.getText().isEmpty()) {
            // If it's the email field, check for validity first
            if (field == emailField && !isValidEmail(field.getText())) {
                field.setStyle("-fx-border-color: red;");

                // Add tooltip for invalid email
                Tooltip emailTooltip = new Tooltip("Please enter a valid email address.");
                Tooltip.install(field, emailTooltip);
            } else {
                field.setStyle(null);  // Reset the style if the field has content
                Tooltip.uninstall(field, null);  // Remove any existing tooltips
            }
        }
    }


    private void resetFieldStyle(DatePicker field) {
        if (field.getValue() != null) {
            field.setStyle(null);  // Reset the style if a date is selected
        }
    }

    private void resetFieldStyle(CheckBox field) {
        if (field.isSelected()) {
            field.setStyle(null);  // Reset the style if a date is selected
        }
    }

    @FXML
    protected void onRegisterClick() {
        // Map to associate fields with their validation conditions
        List<Node> fields = List.of(
                firstNameField,
                lastNameField,
                emailField,
                passwordField,
                confirmPasswordField,
                dateField
        );
        boolean hasEmptyField = false;

        for (Node field : fields) {
            if (field instanceof TextField && ((TextField) field).getText().isEmpty()) {
                field.setStyle("-fx-border-color: red;");
                hasEmptyField = true;
            } else if (field instanceof DatePicker && ((DatePicker) field).getValue() == null) {
                field.setStyle("-fx-border-color: red;");
                hasEmptyField = true;
            } else {
                field.setStyle(null);
            }
        }

        if (hasEmptyField) {
            statusText.setText("Please fill in all fields.");
            return;
        }

        if (!isValidEmail(emailField.getText())) {
            statusText.setText("Please Enter a valid email");
            emailField.setStyle("-fx-border-color: red;");
            return;
        } else {
            emailField.setStyle(null);
        }

        if (!policyCheck.isSelected()) {
            policyCheck.setStyle("-fx-text-fill: red; -fx-accent: red;");
            statusText.setText("Please agree to the terms and privacy policy.");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            statusText.setText("Passwords do not match");
            passwordField.setStyle("-fx-border-color: red;");
            confirmPasswordField.setStyle("-fx-border-color: red;");
            return;
        } else {
            passwordField.setStyle(null);
            confirmPasswordField.setStyle(null);
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        LocalDate date = dateField.getValue();
        String password = passwordField.getText();

        System.out.println("Registration successful!");
        System.out.printf("User Info: %s %s, Email: %s, DOB: %s%n", firstName, lastName, email, date);
    }



    @FXML
    protected void onCancelClick(){
        switchScene("landing");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
