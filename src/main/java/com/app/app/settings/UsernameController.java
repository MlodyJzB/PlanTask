package com.app.app.settings;

import com.app.loginapp.Database;
import com.app.loginapp.LoginController;
import com.app.loginapp.User;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UsernameController implements Initializable {
    private User user;
    @FXML
    TextField usernameTextField;
    @FXML
    Label usernameErrorLabel, usernameChangeStatusLabel;
    @FXML
    Button okButton, cancelButton, applyButton;
    @FXML
    void okClicked(ActionEvent event) {
        if (user.isUsernameChanged()) {
            applyClicked(event);
        }
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    @FXML
    void applyClicked(ActionEvent event) {
        try {
            Database.changeUsername(user.getUsername(), user.getChangedUsername());
            user.setUsername(user.getChangedUsername());
            usernameChangeStatusLabel.setStyle("-fx-text-fill: green");
            usernameChangeStatusLabel.setText("Username changed!");
            String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
            JSONObject o = new JSONObject(contents);
            int a = (int) o.get(("which"));
            LoginController.PutJsonInfo(a, user.getUsername(), true);
        } catch (SQLException e) {
            e.printStackTrace();
            usernameChangeStatusLabel.setStyle("-fx-text-fill: red");
            usernameChangeStatusLabel.setText("Failed to change username!");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
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
        usernameTextField.setText(user.getUsername());

        usernameTextField.textProperty().addListener((ov, oldVal, newVal) -> {
            //If user has left field and field isn't empty
            if (newVal.length() <= 3) {
                user.setUsernameLongEnough(false);
                usernameErrorLabel.setStyle("-fx-text-fill: red");
                usernameErrorLabel.setText("Username too short!");
            } else {
                user.setUsernameLongEnough(true);
                user.usernameChangedProperty().set(!newVal.equals(user.getUsername()));

                user.setChangedUsername(newVal);

                //Checking if username exists and setting appropriate variable in User class
                //Setting appropriate text on usernameErrorLabel
                if (!user.isUsernameChanged()) {
                    usernameErrorLabel.setStyle("-fx-text-fill: red");
                    usernameErrorLabel.setText("Username not changed!");
                } else
                    usernameErrorLabel.setText("");
            }

        });

        applyButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> !(user.isUsernameChanged() && user.isUsernameLongEnough()),
                user.usernameChangedProperty(), user.usernameLongEnoughProperty()
        ));

        LoginController.configureTextFields(new TextField[]{usernameTextField});
    }
}
