package com.loginapp.loginapp;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class RegisterController implements Initializable {
    private LoginInfo loginInfo;
    private BooleanProperty canRegister = new SimpleBooleanProperty();

    @FXML
    private Button registerButton, loginButton;
    @FXML
    private ImageView minCharImageView, minCapLettersImageView, minDigitsImageView;
    @FXML
    private Label minCharLabel, minCapLettersLabel, minDigitsLabel, usernameErrorLabel, repeatedPasswordErrorLabel, registerStatusLabel;
    @FXML
    private TextField usernameTextField, passwordField, repeatedPasswordField;
    @FXML
    private void onClickRegister() throws SQLException {
        loginInfo.setUsername(usernameTextField.getText());
        loginInfo.setPassword(passwordField.getText());
        try {
            loginInfo.addLoginInfoToDatabase();
            registerStatusLabel.setStyle("-fx-text-fill: green");
            registerStatusLabel.setText("Registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            registerStatusLabel.setStyle("-fx-text-fill: red");
            registerStatusLabel.setText("Failed to register!");
        }


    }
    @FXML
    private void switchToLoginScene(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-scene.fxml")));
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setRoot(fxmlLoader);
    }



    private void setConditionStyle(ImageView imageView, Label label, boolean condition) {
        if (condition) {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/tickSymbol.png"))));
            label.setStyle("-fx-text-fill: green");
        } else {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/xSymbol.png"))));
            label.setStyle("-fx-text-fill: red");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginInfo = new LoginInfo();

        usernameTextField.focusedProperty().addListener((ov, oldVal, newVal) -> {
            loginInfo.setUsername(usernameTextField.getText());
            if (!newVal && !Objects.equals(usernameTextField.getText(), "")) {
                boolean usernameAvailable = false;
                try {
                    usernameAvailable = !loginInfo.checkIfUsernameInDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                loginInfo.setUsernameAvailable(usernameAvailable);
                if (loginInfo.isUsernameAvailable()) {
                    usernameErrorLabel.setStyle("-fx-text-fill: green");
                    usernameErrorLabel.setText("Username available");
                }
                else  {
                    usernameErrorLabel.setStyle("-fx-text-fill: red");
                    usernameErrorLabel.setText("Username Already Taken!");
                }
            }
        });

        passwordField.textProperty().addListener((ov, oldVal, newVal) -> {
            List<Boolean> conditionsList = LoginInfo.checkPassword(newVal, 8);
            setConditionStyle(minCharImageView, minCharLabel, conditionsList.get(0));
            setConditionStyle(minCapLettersImageView, minCapLettersLabel, conditionsList.get(1));
            setConditionStyle(minDigitsImageView, minDigitsLabel, conditionsList.get(2));

            loginInfo.setPasswordMeetsConditions(!conditionsList.contains(false));
            canRegister.setValue(!conditionsList.contains(false));
        });

        repeatedPasswordField.textProperty().addListener((ov, oldVal, newVal) -> {
            boolean passwordsMatch = newVal.equals(passwordField.getText());
            loginInfo.setPasswordsMatch(passwordsMatch);
            if (passwordsMatch) {
                repeatedPasswordErrorLabel.setStyle("-fx-text-fill: green");
                repeatedPasswordErrorLabel.setText("Passwords match");
            } else {
                repeatedPasswordErrorLabel.setStyle("-fx-text-fill: red");
                repeatedPasswordErrorLabel.setText("Passwords don't match!");
            }
        });

        registerButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> !(loginInfo.isUsernameAvailable() && loginInfo.isPasswordMeetsConditions() && loginInfo.isPasswordsMatch()),
                loginInfo.usernameAvailableProperty(), loginInfo.passwordMeetsConditionsProperty(), loginInfo.passwordsMatchProperty()
        ));

        LoginController.configureTextFields(new TextField[]{usernameTextField, passwordField});

    }
}
