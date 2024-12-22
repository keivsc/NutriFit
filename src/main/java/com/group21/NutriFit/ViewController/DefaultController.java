package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.App;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.SharedData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultController {
    private static SharedData sharedData;
    private static Stage primaryStage;

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
        System.out.println(fxmlLoader.getLocation());
        try {
            Scene scene = new Scene(fxmlLoader.load());

        DefaultController controller = fxmlLoader.getController();
        scene.getStylesheets().add(App.class.getResource("/com/group21/nutrifit/view/src/style.css").toExternalForm());
        controller.setStage(primaryStage);
        primaryStage.setScene(scene);
        }catch(IOException e){
            System.out.println(e);
            Logger.getLogger(getClass().getName()).severe("Unable to find specified view");
            System.exit(1);
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
}

