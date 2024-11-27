module com.group21.NutriFit {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group21.NutriFit to javafx.fxml;
    exports com.group21.NutriFit;
    exports com.group21.NutriFit.database;
    opens com.group21.NutriFit.database to javafx.fxml;
    exports com.group21.NutriFit.Scenes;
    opens com.group21.NutriFit.Scenes to javafx.fxml;
}