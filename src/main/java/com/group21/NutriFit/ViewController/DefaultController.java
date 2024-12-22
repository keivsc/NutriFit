package com.group21.NutriFit.ViewController;

import com.group21.NutriFit.App;
import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.utils.SharedData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

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
}
