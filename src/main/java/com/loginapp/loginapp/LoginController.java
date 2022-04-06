package com.loginapp.loginapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private LoginInfo loginInfo;

    @FXML
    Text loginStatusText;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton, onClickRegisterScene;
    @FXML
    private void onClickLogin() throws SQLException {
        loginInfo.setUsername(usernameTextField.getText());
        loginInfo.setPassword(passwordField.getText());
        if (loginInfo.checkIfUserInDatabase()) {
            loginStatusText.setStyle("-fx-fill: green");
            loginStatusText.setText("Logged successfully");
        }
        else  {
            loginStatusText.setStyle("-fx-fill: #b71834");
            loginStatusText.setText("Incorrect username/password");
        }
    }
    @FXML
    private void switchToRegisterScene(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register-scene.fxml")));
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setRoot(fxmlLoader);
    }

    public static void limitMaxChar(TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                tf.setText(newValue.substring(0, maxLength));
            }
        });
    }

    public static void configureTextFields(TextField[] textFields) {
        for (TextField tf : textFields) {
            limitMaxChar(tf, 20);
            tf.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().contains(" ")) {
                    change.setText(change.getText().replace(" ", ""));
                }
                return change;
            }));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginInfo = new LoginInfo();
        configureTextFields(new TextField[]{usernameTextField, passwordField});
    }


}