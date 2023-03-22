module com.battleshipgalactica.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.battleshipgalactica.model;
                            
    opens com.battleshipgalactica.gui to javafx.fxml;
    exports com.battleshipgalactica.gui;
}