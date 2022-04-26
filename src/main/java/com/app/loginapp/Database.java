package com.app.loginapp;

import java.sql.*;

public class Database {

    static boolean checkIfUserExists(String username, String password) {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC GetUser @username = ?, @password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean checkIfUsernameExists(String username) {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC GetUsername @username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void addUser(String username, String password) throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC AddUser @username = ?, @password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void ChangePassword(String username, String oldPassword, String newPassword) throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC ChangePassword @username = ?, @oldPassword = ?, @newPassword = ?");
            statement.setString(1, username);
            statement.setString(2, oldPassword);
            statement.setString(2, newPassword);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void ChangeUsername(String username, String password, String newUsername) throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC ChangeUsername @username = ?, @password = ?, @newUsername = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(2, newUsername);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
