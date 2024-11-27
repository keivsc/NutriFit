package com.group21.NutriFit.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LandingController extends Default{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoginClick() {
        System.out.println("LOGIN CLICKED");
    }

    @FXML
    protected void onRegisterClick(){
        System.out.println("REGISTER CLICKED");
    }
}