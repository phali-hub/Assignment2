module skool.assignment22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.media;


    opens skool.assignment22 to javafx.fxml;
    exports skool.assignment22;

}