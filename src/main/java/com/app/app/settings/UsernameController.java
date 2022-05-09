package com.app.app.settings;

import com.app.loginapp.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UsernameController implements Initializable {
    private User user;
    private final BooleanProperty usernameChanged = new SimpleBooleanProperty(false);
    @FXML
    TextField usernameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        usernameTextField.setText(user.getUsername());
        usernameTextField.textProperty().addListener((ov, oldVal, newVal) -> {
            usernameChanged.set(!newVal.equals(user.getUsername()));
        });

    }
}
