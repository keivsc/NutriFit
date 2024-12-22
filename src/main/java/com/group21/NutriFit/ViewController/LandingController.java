package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.Encryption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LandingController extends DefaultController {
    public Button registerButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Text loginStatus;

    @FXML
    protected void onLoginClick() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            loginStatus.setText("Please enter your email and password.");
        } else if (!isValidEmail(email)) {
            loginStatus.setText("Please enter a valid email.");
        } else {
            String keys = User.authorise(email, password);
            if (keys == null || keys.isEmpty()) {
                loginStatus.setText("Incorrect email or password.");
            } else {
                getSharedData().setEncryptionkeys(Encryption.parseKeys(keys));
                switchScene("home");
            }
        }

        System.out.println("LOGIN CLICKED");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    protected void onSignUpClick(){
        switchScene("SignUp");
    }

    public void onForgotPasswordClick(ActionEvent actionEvent) {
    }
}