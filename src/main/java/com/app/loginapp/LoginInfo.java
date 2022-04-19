package com.app.loginapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.*;
import java.util.Arrays;
import java.util.List;


public class LoginInfo {
    static LoginInfo  loginInfo = new LoginInfo();

    private String username, password;
    private final BooleanProperty usernameAvailable = new SimpleBooleanProperty(false);
    private final BooleanProperty usernameLongEnough = new SimpleBooleanProperty(false);
    private final BooleanProperty passwordMeetsConditions = new SimpleBooleanProperty(false);
    private final BooleanProperty passwordsMatch = new SimpleBooleanProperty(false);


    private LoginInfo() {
        username = "";
        password = "";
    }

    public static LoginInfo getInstance() {
        return loginInfo;
    }


    public boolean isUsernameLongEnough() { return usernameLongEnough.get(); }

    public BooleanProperty usernameLongEnoughProperty() { return usernameLongEnough; }

    public void setUsernameLongEnough(boolean usernameLongEnough) { this.usernameLongEnough.set(usernameLongEnough); }

    public boolean isUsernameAvailable() {
        return usernameAvailable.get();
    }

    public BooleanProperty usernameAvailableProperty() {
        return usernameAvailable;
    }

    public void setUsernameAvailable(boolean usernameAvailable) {
        this.usernameAvailable.set(usernameAvailable);
    }

    public boolean isPasswordMeetsConditions() {
        return passwordMeetsConditions.get();
    }

    public BooleanProperty passwordMeetsConditionsProperty() {
        return passwordMeetsConditions;
    }

    public void setPasswordMeetsConditions(boolean passwordMeetsConditions) { this.passwordMeetsConditions.set(passwordMeetsConditions); }

    public boolean isPasswordsMatch() {
        return passwordsMatch.get();
    }

    public BooleanProperty passwordsMatchProperty() {
        return passwordsMatch;
    }

    public void setPasswordsMatch(boolean passwordsMatch) {
        this.passwordsMatch.set(passwordsMatch);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean checkIfUserInDatabase() throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("SELECT username, password FROM loginInfo WHERE username= ? AND password= ? ");
            statement.setString(1, getUsername());
            statement.setString(2, getPassword());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfUsernameInDatabase() throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("SELECT username FROM loginInfo WHERE username= ?");
            statement.setString(1, getUsername());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addLoginInfoToDatabase() throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("INSERT INTO loginInfo(username, password) VALUES (?, ?)");
            statement.setString(1, getUsername());
            statement.setString(2, getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Password should contain at least 8 characters, one capital letter and one number
    public static List<Boolean> checkPassword(String str, int minChar) {
        char ch;
        boolean minCharFlag = str.length() >= minChar;
        boolean capitalFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch))
                capitalFlag = true;
            else if (Character.isDigit(ch))
                numberFlag = true;
        }
        return Arrays.asList(minCharFlag, capitalFlag, numberFlag);
    }

}
