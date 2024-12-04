package com.group21.NutriFit.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController extends DefaultController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoginClick() {
        System.out.println("LOGIN CLICKED");
    }
}