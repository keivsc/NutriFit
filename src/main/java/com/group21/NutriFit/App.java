package com.group21.NutriFit;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("NutriFit");
        stage.setResizable(false);
        stage.setHeight(810);
        stage.setWidth(1440);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
