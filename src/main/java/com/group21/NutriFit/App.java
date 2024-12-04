package com.group21.NutriFit;

import com.group21.NutriFit.ViewController.DefaultController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/landing.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        DefaultController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("NutriFit");
        stage.setMinHeight(810);
        stage.setMinWidth(1440);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
