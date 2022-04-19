package com.app.loginapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {
    private LoginInfo loginInfo;
    @FXML
    public void goToLogin() throws IOException {
        FXMLLoader pane = new FXMLLoader(getClass().getResource("register-scene.fxml"));
        Parent root = (Parent) pane.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginInfo = LoginInfo.getInstance();
    }
}
