package com.app.loginapp;

import javafx.beans.binding.Bindings;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class RegisterController implements Initializable {
    private User user;
    @FXML
    public ImageView unregistering;
    public void Exit1(MouseEvent mouseEvent) {
        ((Stage) unregistering.getScene().getWindow()).close();
    }
    @FXML
    private Button registerButton;
    @FXML
    private ImageView minCharImageView, minCapLettersImageView, minDigitsImageView;
    @FXML
    private Label minCharLabel, minCapLettersLabel, minDigitsLabel, usernameErrorLabel, repeatedPasswordErrorLabel, registerStatusLabel;
    @FXML
    private TextField usernameTextField, passwordField, repeatedPasswordField;
    @FXML //Function called on click of the registerButton
    private void onClickRegister() {
        user.setUsername(usernameTextField.getText());
        user.setPassword(passwordField.getText());
        try {
            Database.addUser(user.getUsername(), user.getPassword());
            Database.addAppearance(user.getUsername());
            registerStatusLabel.setStyle("-fx-text-fill: green");
            registerStatusLabel.setText("Registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            registerStatusLabel.setStyle("-fx-text-fill: red");
            registerStatusLabel.setText("Failed to register!");
        }
    }
    @FXML //Function called on click of the registerButton
    private void onEnterRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !registerButton.disableProperty().getValue())
            onClickRegister();
    }

    @FXML //Function called on click of the loginButton
    private void switchToLoginScene(ActionEvent event) throws IOException {
        //Loading new scene from fxml file on click
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-scene.fxml")));
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setRoot(fxmlLoader);
    }

    public static void setConditionStyle(ImageView imageView, Label label, boolean conditionTrue) {
        //Set different styles of password condition dependent on conditionTrue
        if (conditionTrue) {
            imageView.setImage(new Image(Objects.requireNonNull(RegisterController.class.getResourceAsStream("/Images/tickSymbol.png"))));
            label.setStyle("-fx-text-fill: green");
        } else {
            imageView.setImage(new Image(Objects.requireNonNull(RegisterController.class.getResourceAsStream("/Images/xSymbol.png"))));
            label.setStyle("-fx-text-fill: red");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();

        //Creating listener to check whether user has clicked out of usernameTextField and then
        //whether user is in database and setting appropriate text on usernameErrolLabel
        usernameTextField.focusedProperty().addListener((ov, oldVal, newVal) -> {
            //If user has left field and field isn't empty
            if (!newVal) {
                if (usernameTextField.getText().length() < 3) {
                    user.setUsernameLongEnough(false);
                    usernameErrorLabel.setStyle("-fx-text-fill: red");
                    usernameErrorLabel.setText("Username too short!");
                } else {
                    user.setUsername(usernameTextField.getText());
                    user.setUsernameLongEnough(true);

                    //Checking if username exists and setting appropriate variable in User class
                    user.setUsernameAvailable(!Database.checkIfUserExists(user.getUsername()));
                    //Setting appropriate text on usernameErrorLabel
                    if (user.isUsernameAvailable()) {
                        usernameErrorLabel.setStyle("-fx-text-fill: green");
                        usernameErrorLabel.setText("Username available");
                    }
                    else  {
                        usernameErrorLabel.setStyle("-fx-text-fill: red");
                        usernameErrorLabel.setText("Username Already Taken!");
                    }
                }

            }
        });

        //Creating listener to check whether password meets conditions and setting appropriate styles based on it
        passwordField.textProperty().addListener((ov, oldVal, newVal) -> {
            List<Boolean> conditionsList = User.checkPassword(newVal, 8);
            setConditionStyle(minCharImageView, minCharLabel, conditionsList.get(0));
            setConditionStyle(minCapLettersImageView, minCapLettersLabel, conditionsList.get(1));
            setConditionStyle(minDigitsImageView, minDigitsLabel, conditionsList.get(2));
            //Setting if password meets all conditions
            user.setPasswordMeetsConditions(!conditionsList.contains(false));
        });

        //Creating listener to check whether password and repeated passwords match
        repeatedPasswordField.textProperty().addListener((ov, oldVal, newVal) -> {
            boolean passwordsMatch = newVal.equals(passwordField.getText());
            user.setPasswordsMatch(passwordsMatch);
            if (passwordsMatch) {
                repeatedPasswordErrorLabel.setStyle("-fx-text-fill: green");
                repeatedPasswordErrorLabel.setText("Passwords match");
            } else {
                repeatedPasswordErrorLabel.setStyle("-fx-text-fill: red");
                repeatedPasswordErrorLabel.setText("Passwords don't match!");
            }
        });

        //Binding registerButton.disableProperty() to conditions required for user to register
        registerButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> !(user.isUsernameAvailable() && user.isPasswordMeetsConditions() && user.isPasswordsMatch() && user.isUsernameLongEnough()),
                user.usernameAvailableProperty(), user.passwordMeetsConditionsProperty(), user.passwordsMatchProperty(), user.usernameLongEnoughProperty()
        ));

        //Configuring usernameTextField, passwordField so user can't put too much text
        // and can't have spaces in username of password
        LoginController.configureTextFields(new TextField[]{usernameTextField, passwordField, repeatedPasswordField});

    }
}
