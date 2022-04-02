package com.loginapp.loginapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    Text loginStatusText;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private void onClickLogin() throws SQLException {
        //addLoginInfoToDatabase(usernameTextField.getText(), passwordTextField.getText());
        boolean loginInDatabase = checkIfLoginInfoInDatabase(usernameTextField.getText(), passwordField.getText());
        if (loginInDatabase) {
            loginStatusText.setStyle("-fx-fill: green");
            loginStatusText.setText("Logged successfully");
        }
        else  {
            loginStatusText.setStyle("-fx-fill: red");
            loginStatusText.setText("Incorrect username/password");
        }
    }

    private static void addLoginInfoToDatabase(String newUsername, String newPassword) throws SQLException{
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO loginInfo(username, password) VALUES ('"+newUsername+"', '"+newPassword+"')");
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println(e);
        }
    }

    private static boolean checkIfLoginInfoInDatabase(String giverUsername, String givenPassword) throws SQLException{
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("SELECT username, password FROM loginInfo WHERE username= ? AND password= ? ");
            statement.setString(1, giverUsername);
            statement.setString(2, givenPassword);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println(e);
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}