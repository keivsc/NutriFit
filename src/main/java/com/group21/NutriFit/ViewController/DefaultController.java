package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.App;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.SharedData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultController {
    private User user;
    private Byte profilePicture;
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

    public void switchScene(String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + sceneName + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        DefaultController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(scene);
    }

    public void showNavBar(boolean status){
        //primaryStage.getScene().;
    }

    //home controllers
    //sidebar controllers
    //navigation controllers
}
