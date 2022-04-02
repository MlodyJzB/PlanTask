module com.loginapp.loginapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.loginapp.loginapp to javafx.fxml;
    exports com.loginapp.loginapp;
}