module com.loginapp.loginapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ical4j;
    requires json;
    requires org.jetbrains.annotations;
    requires org.apache.commons.io;


    opens com.app.loginapp to javafx.fxml;
    exports com.app.loginapp;

    opens com.app.app to javafx.fxml;
    exports com.app.app;
}