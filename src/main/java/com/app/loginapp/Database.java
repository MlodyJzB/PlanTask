package com.app.loginapp;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Database {
    static final String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;" +
            "database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;" +
            "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    static boolean checkIfUserExists(String username, String password) {
        try {
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC GetUser @username = ?, @password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            statement.close();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkIfUsernameExists(String username) {
        try {
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC GetUsername @username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            statement.close();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void addUser(String username, String password) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC AddUser @username = ?, @password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC ChangePassword @username = ?, " + "@oldPassword = ?, @newPassword = ?");
            statement.setString(1, username);
            statement.setString(2, oldPassword);
            statement.setString(3, newPassword);
            final int status = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void changeUsername(String username, String newUsername) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeUsername @username = ?, @newUsername = ?");
        statement.setString(1, username);
        statement.setString(2, newUsername);
        statement.executeUpdate();
        statement.close();
//        if (statement.executeUpdate() == -1)
//            throw new SQLException("Failed to change Username!");
    }

    public static void addEvent(String title, String username, String startDateTime,
                                String endDateTime, boolean fullDay) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC AddEvent @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?, @fullDay = ?");
        statement.setString(1, title);
        statement.setString(2, username);
        statement.setString(3, startDateTime);
        statement.setString(4, endDateTime);
        statement.setString(5, String.valueOf(fullDay));
//        System.out.println(title + "\n" + username + "\n" + startDateTime + "\n" + endDateTime);
        statement.executeUpdate();
        statement.close();
    }

    public static List<List<String>> getUserEventsAsString(String username,
                                                           String startRangeDateTime, String endRangeDateTime) {
        try {
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC GetUserEventsAsString @user = ?, @startRange = ?, @endRange = ?");
            statement.setString(1, username);
            statement.setString(2, startRangeDateTime);
            statement.setString(3, endRangeDateTime);
            ResultSet resultSet = statement.executeQuery();

            List<List<String>> userEventsList = new ArrayList<>();
            while (resultSet.next()) {
                userEventsList.add(List.of(
                        resultSet.getString("title"),
                        resultSet.getString("startDateTime"),
                        resultSet.getString("endDateTime"),
                        resultSet.getString("fullDay"),
                        resultSet.getString("recurring"),
                        resultSet.getString("rrule")
                        ));
            }
            return userEventsList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void deleteEvent(String title, String username,
                                   String startDateTime, String endDateTime) throws SQLException{
            Connection con = DriverManager.getConnection(connectionString);
            PreparedStatement statement = con.prepareStatement("EXEC DeleteRowFromEvents @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?");
            statement.setString(1, title);
            statement.setString(2, username);
            statement.setString(3, startDateTime);
            statement.setString(4, endDateTime);
            statement.executeUpdate();
        statement.close();
    }

    public static void changeEventTitle(String username, String newTitle, String oldTitle,
                                        String startDateTime, String endDateTime) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeEventTitle @user = ?, @oldTitle = ?, " + "@newTitle = ?, @startDateTime = ?, @endDateTime = ?");
        statement.setString(1, username);
        statement.setString(2, oldTitle);
        statement.setString(3, newTitle);
        statement.setString(4, startDateTime);
        statement.setString(5, endDateTime);
        statement.executeUpdate();
        statement.close();
    }

    public static void changeEventInterval(String username, String title, String oldStartDateTime, String oldEndDateTime,
                                           String newStartDateTime, String newEndDateTime) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeEventInterval @user = ?, @title = ?, @oldStartDateTime = ?, @oldEndDateTime = ?, @newStartDateTime = ?, @newEndDateTime = ?");
        statement.setString(1, username);
        statement.setString(2, title);
        statement.setString(3, oldStartDateTime);
        statement.setString(4, oldEndDateTime);
        statement.setString(5, newStartDateTime);
        statement.setString(6, newEndDateTime);
        statement.executeUpdate();
        statement.close();
    }
    public static void changeEventFullDay(String username, String title, String startDateTime, String endDateTime,
                                          boolean oldFullDay, boolean newFullDay) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeEventFullDay @user = ?, @title = ?, @startDateTime = ?, @endDateTime = ?, @oldFullDay = ?, @newFullDay = ?");
        statement.setString(1, username);
        statement.setString(2, title);
        statement.setString(3, startDateTime);
        statement.setString(4, endDateTime);
        statement.setString(5, String.valueOf(oldFullDay));
        statement.setString(6, String.valueOf(newFullDay));
        statement.executeUpdate();
        statement.close();
    }
    public static void ChangeEventRecurringAndRrule(String username, String title,
                                                    String startDateTime, String endDateTime,
                                                    boolean recurring, String rrule) throws SQLException {
        Connection con = DriverManager.getConnection(connectionString);
        PreparedStatement statement = con.prepareStatement("EXEC ChangeEventRecurringAndRrule @user = ?, @title = ?, @startDateTime = ?, @endDateTime = ?, @recurring = ?, @rrule = ?");
        statement.setString(1, username);
        statement.setString(2, title);
        statement.setString(3, startDateTime);
        statement.setString(4, endDateTime);
        statement.setString(5, String.valueOf(recurring));
        statement.setString(6, rrule);
        statement.executeUpdate();
        statement.close();
    }
}
