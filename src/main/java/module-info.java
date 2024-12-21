module com.group21.NutriFit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.logging;


    opens com.group21.NutriFit to javafx.fxml;
    exports com.group21.NutriFit;
    exports com.group21.NutriFit.ViewController;
    opens com.group21.NutriFit.ViewController to javafx.fxml;
    exports com.group21.NutriFit.utils;
    opens com.group21.NutriFit.utils to javafx.fxml;
}