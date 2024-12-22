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
        // Load FXML - make sure the path is absolute
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/group21/NutriFit/view/Landing.fxml"));

        // Create the Scene
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        DefaultController controller = fxmlLoader.getController();
        controller.setStage(stage);

        // Load the CSS file correctly (absolute path in the JAR)
        scene.getStylesheets().add(App.class.getResource("/com/group21/NutriFit/view/src/style.css").toExternalForm());

        // Configure the Stage
        stage.setTitle("NutriFit");
        stage.setMinHeight(810);
        stage.setMinWidth(1440);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
