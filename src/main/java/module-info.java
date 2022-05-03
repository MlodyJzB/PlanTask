module com.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ical4j;
    requires json;
    requires org.jetbrains.annotations;
    requires org.apache.commons.io;
    requires json.simple;


    opens com.app.loginapp to javafx.fxml;
    exports com.app.loginapp;

    opens com.app.app to javafx.fxml;
    exports com.app.app;
    exports com.app.app.settings;
    opens com.app.app.settings to javafx.fxml;
}