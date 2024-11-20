module com.group21.NutriFit {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group21.NutriFit to javafx.fxml;
    exports com.group21.NutriFit;
}