package com.loginapp.loginapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class LoginInfo {
    private String username, password;
    private final BooleanProperty usernameAvailable = new SimpleBooleanProperty();
    private final BooleanProperty passwordMeetsConditions = new SimpleBooleanProperty();
    private final BooleanProperty passwordsMatch = new SimpleBooleanProperty();

    public LoginInfo() {
        username = ""; password = "";
    }

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

    public void setPasswordMeetsConditions(boolean passwordMeetsConditions) {
        this.passwordMeetsConditions.set(passwordMeetsConditions);
    }

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
            //System.out.println(e);
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
            //System.out.println(e);
        }
        return false;
    }

    public void addLoginInfoToDatabase() throws SQLException{
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO loginInfo(username, password) VALUES ('"+this.username+"', '"+this.password+"')");
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println(e);
        }
    }

    public static List<Boolean> checkPassword(String str, int minChar) {
        char ch;
        boolean minCharFlag = str.length() >= minChar;
        boolean capitalFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if ( Character.isUpperCase(ch) )
                capitalFlag = true;
            else if (Character.isDigit(ch))
                numberFlag = true;
        }
        return Arrays.asList(minCharFlag, capitalFlag, numberFlag);
    }

}
