package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.Encryption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class LandingController extends DefaultController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Text statusText;

    private String defaultStyle;

    @FXML
    protected void initialize() {
        defaultStyle = emailField.getStyle();
        emailField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(emailField, defaultStyle));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> resetFieldStyle(passwordField, defaultStyle));
    }

    @FXML
    protected void onLoginClick() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusText.setText("Please enter your email and password.");
            if (email.isEmpty()) emailField.setStyle("-fx-border-color:red;");
            if (password.isEmpty()) passwordField.setStyle("-fx-border-color:red;");
            return;
        }

        if (!isValidEmail(email)) {
            statusText.setText("Please enter a valid email.");
            emailField.setStyle(emailField.getStyle()+"-fx-border-color:red;");
            return;
        }

        String keys = User.authorise(email, password);
        if (keys == null || keys.isEmpty()) {
            statusText.setText("Incorrect email or password.");
            emailField.setStyle(emailField.getStyle()+"-fx-border-color:red;");
            passwordField.setStyle(passwordField.getStyle()+"-fx-border-color:red;");
        } else {
            getSharedData().setEncryptionkeys(Encryption.parseKeys(keys));
            switchScene("Home");
        }
    }

    @FXML
    public void onSignUpClick() {
        switchScene("SignUp");
    }

    public void onForgotPasswordClick(ActionEvent actionEvent) {
    }
}