package com.app.app.settings;

import com.app.loginapp.Database;
import com.app.loginapp.LoginController;
import com.app.loginapp.RegisterController;
import com.app.loginapp.User;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class PasswordController implements Initializable {
    private User user;
    @FXML
    TextField oldPasswordTextField, newPasswordTextField, repeatedNewPasswordTextField;
    @FXML
    Button okButton, cancelButton, applyButton;
    @FXML
    private ImageView minCharImageView, minCapLettersImageView, minDigitsImageView;
    @FXML
    private Label minCharLabel, minCapLettersLabel, minDigitsLabel,
            repeatedPasswordErrorLabel, passwordChangeStatusLabel;
    @FXML
    void okClicked(ActionEvent event) {
        if (!applyButton.disableProperty().getValue()) {
            try {
                Database.changePassword(user.getUsername(), user.getPassword(), user.getChangedPassword());
                user.setPassword(user.getChangedPassword());
                passwordChangeStatusLabel.setStyle("-fx-text-fill: green");
                passwordChangeStatusLabel.setText("Password changed!");
            } catch (SQLException e) {
                e.printStackTrace();
                passwordChangeStatusLabel.setStyle("-fx-text-fill: red");
                passwordChangeStatusLabel.setText("Failed to change password!");
            }
        }
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    @FXML
    void applyClicked(ActionEvent event) {
        try {
            System.out.println(user.getUsername() + " "
                    + user.getPassword() + " "
                    + user.getChangedPassword());
            Database.changePassword(user.getUsername(), user.getPassword(), user.getChangedPassword());
            user.setPassword(user.getChangedPassword());
            passwordChangeStatusLabel.setStyle("-fx-text-fill: green");
            passwordChangeStatusLabel.setText("Password changed!");
        } catch (SQLException e) {
            e.printStackTrace();
            passwordChangeStatusLabel.setStyle("-fx-text-fill: red");
            passwordChangeStatusLabel.setText("Failed to change password!");
        }

    }
    @FXML
    void cancelClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();

        //Creating listener to check whether password meets conditions and setting appropriate styles based on it
        newPasswordTextField.textProperty().addListener((ov, oldVal, newVal) -> {
            List<Boolean> conditionsList = User.checkPassword(newVal, 8);
            RegisterController.setConditionStyle(minCharImageView, minCharLabel, conditionsList.get(0));
            RegisterController.setConditionStyle(minCapLettersImageView, minCapLettersLabel, conditionsList.get(1));
            RegisterController.setConditionStyle(minDigitsImageView, minDigitsLabel, conditionsList.get(2));
            //Setting if password meets all conditions
            user.setPasswordMeetsConditions(!conditionsList.contains(false));
            user.setChangedPassword(newVal);
        });

        //Creating listener to check whether password and repeated passwords match
        repeatedNewPasswordTextField.textProperty().addListener((ov, oldVal, newVal) -> {
            boolean passwordsMatch = newVal.equals(newPasswordTextField.getText());
            user.setPasswordsMatch(passwordsMatch);
            if (passwordsMatch) {
                repeatedPasswordErrorLabel.setStyle("-fx-text-fill: green");
                repeatedPasswordErrorLabel.setText("Passwords match");
            } else {
                repeatedPasswordErrorLabel.setStyle("-fx-text-fill: red");
                repeatedPasswordErrorLabel.setText("Passwords don't match!");
            }
        });

        applyButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> !(user.isPasswordMeetsConditions() && user.isPasswordsMatch()),
                user.passwordMeetsConditionsProperty(), user.passwordsMatchProperty()
        ));

        LoginController.configureTextFields(new TextField[]{newPasswordTextField, repeatedNewPasswordTextField, oldPasswordTextField});
    }
}
