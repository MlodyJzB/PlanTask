package com.app.loginapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean checkIfUsernameExists(String username) {
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

    public static void changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC ChangePassword @username = ?, @oldPassword = ?, @newPassword = ?");
            statement.setString(1, username);
            statement.setString(2, oldPassword);
            statement.setString(3, newPassword);
            final int status = statement.executeUpdate();
            System.out.println(status);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void changeUsername(String username, String newUsername) throws SQLException {
        String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeUsername @username = ?, @newUsername = ?");
        statement.setString(1, username);
        statement.setString(2, newUsername);
        statement.executeUpdate();
//        if (statement.executeUpdate() == -1)
//            throw new SQLException("Failed to change Username!");
    }

    public static void addEvent(String title, String username, String startDateTime, String endDateTime) throws SQLException {
        String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC AddEvent @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?");
        statement.setString(1, title);
        statement.setString(2, username);
        statement.setString(3, startDateTime.toString());
        statement.setString(4, endDateTime.toString());
        statement.executeUpdate();
    }

    public static List<List<String>> getUserEventsAsString(String username) {
        try {
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC GetUserEventsAsString @user = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            List<List<String>> userEventsList = new ArrayList<>();
            while (resultSet.next()) {
                userEventsList.add(List.of(
                        resultSet.getString("title"),
                        resultSet.getString("startDateTime"),
                        resultSet.getString("endDateTime")
                        )
                );
            }
            return userEventsList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void deleteEvent(String title, String username, String startDateTime, String endDateTime) throws SQLException{
            String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC DeleteRowFromEvents @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?");
            statement.setString(1, title);
            statement.setString(2, username);
            statement.setString(3, startDateTime.toString());
            statement.setString(4, endDateTime.toString());
            statement.executeUpdate();
    }

    public static void changeEvent(String username, String oldTitle, String oldStartDateTime, String oldEndDateTime,
                                   String newTitle, String newStartDateTime, String newEndDateTime) throws SQLException {
        String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeRowInEvents @user = ?, @oldTitle = ?, @oldStartDateTime = ?, @oldEndDateTime = ?, @newTitle = ?, @newStartDateTime = ?, @newEndDateTime = ?");
        statement.setString(1, username);
        statement.setString(2, oldTitle);
        statement.setString(3, oldStartDateTime.toString());
        statement.setString(4, oldEndDateTime.toString());
        statement.setString(2, newTitle);
        statement.setString(3, newStartDateTime.toString());
        statement.setString(4, newEndDateTime.toString());
        statement.executeUpdate();
    }
}
