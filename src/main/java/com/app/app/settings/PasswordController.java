package com.app.app.settings;

import com.app.loginapp.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordController implements Initializable {
    private User user;
    @FXML
    TextField passwordTextField, oldPasswordTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        passwordTextField.setText(user.getPassword());
        passwordTextField.setEditable(false);
    }
}
