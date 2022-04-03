package com.loginapp.loginapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class RegisterController implements Initializable {
    private LoginInfo loginInfo;

    @FXML
    private Button registerButton, loginButton;
    @FXML
    private ImageView minCharImageView, minCapLettersImageView, minDigitsImageView;
    @FXML
    private Label minCharLabel, minCapLettersLabel, minDigitsLabel, usernameErrorLabel;
    @FXML
    private TextField newUsernameTextField, passwordField, repeatedPasswordField;
    @FXML
    private void onClickRegister() throws SQLException {
        loginInfo.setUsername(newUsernameTextField.getText());
        loginInfo.setPassword(passwordField.getText());
        loginInfo.addLoginInfoToDatabase();
    }
    @FXML
    private void switchToLoginScene(){

    }



    private void setConditionStyle(ImageView imageView, Label label, boolean condition) {
        if (condition) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/Images/tickSymbol.png")));
            label.setStyle("-fx-text-fill: green");
        } else {
            imageView.setImage(new Image(getClass().getResourceAsStream("/Images/xSymbol.png")));
            label.setStyle("-fx-text-fill: red");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginInfo = new LoginInfo();

        newUsernameTextField.focusedProperty().addListener((ov, oldVal, newVal) -> {
            loginInfo.setUsername(newUsernameTextField.getText());
            if (!newVal) {
                boolean usernameAlreadyTaken = false;
                try {
                    usernameAlreadyTaken = loginInfo.checkIfUsernameInDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (usernameAlreadyTaken) usernameErrorLabel.setText("Username Already Taken!");
                else  usernameErrorLabel.setText("");
            }
        });

        passwordField.textProperty().addListener((ov, oldVal, newVal) -> {
            List<Boolean> conditionsList = LoginInfo.checkPassword(newVal, 8);
            setConditionStyle(minCharImageView, minCharLabel, conditionsList.get(0));
            setConditionStyle(minCapLettersImageView, minCapLettersLabel, conditionsList.get(1));
            setConditionStyle(minDigitsImageView, minDigitsLabel, conditionsList.get(2));
        });
    }
}
