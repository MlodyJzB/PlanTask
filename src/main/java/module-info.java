module com.loginapp.loginapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ical4j;
    requires json;


    opens com.loginapp.loginapp to javafx.fxml;
    exports com.loginapp.loginapp;
}