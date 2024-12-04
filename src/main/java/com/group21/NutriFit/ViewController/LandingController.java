package com.group21.NutriFit.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class LandingController extends DefaultController {
    public Button registerButton;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoginClick() throws IOException {
        switchScene("login");
        System.out.println("LOGIN CLICKED");
    }

    @FXML
    protected void onRegisterClick(){
        registerButton.setText("NO");

    }
}