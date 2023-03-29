module com.galactica.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.galactica.model;


    opens com.galactica.gui to javafx.fxml;
    exports com.galactica.gui;
}