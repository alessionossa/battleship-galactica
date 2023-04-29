module com.galactica.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.galactica.model;

    opens com.galactica.gui.view to javafx.fxml;
    exports com.galactica.gui.view;
}