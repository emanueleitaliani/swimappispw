module org.example {
    // Moduli JavaFX necessari
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.logging;

    opens org.example to javafx.fxml;
    opens Gui to javafx.fxml;
    opens Bean to javafx.base, javafx.fxml;
    // Questo permette a JavaFX di caricare LoginGui

    exports org.example;
    exports Gui;
    exports Bean;
}