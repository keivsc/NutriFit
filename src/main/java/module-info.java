module com.group21.fta {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group21.fta to javafx.fxml;
    exports com.group21.fta;
}