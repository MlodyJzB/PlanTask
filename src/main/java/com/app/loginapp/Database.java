package com.app.loginapp;

import com.app.app.Event;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import javafx.concurrent.Task;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Database {
    static final String connectionString = "jdbc:sqlserver://plan-task-server.database.windows.net:1433;" +
            "database=planTask;user=JakubNitkiewicz;password=planTask123;encrypt=true;" +
            "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    public static final DateTimeFormatter databaseDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    private record CheckIfUserExistsCallable(String username) implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                Connection con = DriverManager.getConnection(connectionString);
                PreparedStatement statement = con.prepareStatement("EXEC GetUser @username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                boolean userExists = resultSet.next();
                statement.close();
                return userExists;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    static boolean checkIfUserExists(String username) {
        Callable<Boolean> callable = new CheckIfUserExistsCallable(username);
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        Future<Boolean> future = executor.submit(callable);
        executor.shutdown();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return true;
        }
    }


    static void addUser(String username, String password) throws SQLException {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
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
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.submit(task);
        executor.shutdown();
    }

    public static void changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC ChangePassword @username = ?, " + "@oldPassword = ?, @newPassword = ?");
                    statement.setString(1, username);
                    statement.setString(2, oldPassword);
                    statement.setString(3, newPassword);
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.submit(task);
        executor.shutdown();
    }

    public static void changeUsername(String username, String newUsername) throws SQLException {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC ChangeUsername @username = ?, @newUsername = ?");
                    statement.setString(1, username);
                    statement.setString(2, newUsername);
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.submit(task);
        executor.shutdown();
    }

    public static void addEvent(Entry<?> entry, String username) {
        Event event = Event.toEvent(entry);
        addEvent(event, username);
    }

    public static void addEvent(Event event, String username) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC AddEvent @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?, @fullDay = ?");
                    statement.setString(1, event.getTitle());
                    statement.setString(2, username);
                    statement.setString(3, event.getStartDateTimeString());
                    statement.setString(4, event.getEndDateTimeString());
                    statement.setBoolean(5, event.isFullDay());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }

    public static void addEvents(List<Event> eventList, String username) {
        Runnable runnable = () -> {
            try {
                Connection con = DriverManager.getConnection(connectionString);
                PreparedStatement statement = con.prepareStatement("EXEC AddEvent @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?, @fullDay = ?");
                for (Event event : eventList) {
                    statement.setString(1, event.getTitle());
                    statement.setString(2, username);
                    statement.setString(3, event.getStartDateTimeString());
                    statement.setString(4, event.getEndDateTimeString());
                    statement.setBoolean(5, event.isFullDay());
                    statement.addBatch();
                }
                statement.executeBatch();
                statement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.submit(runnable);
        executor.shutdown();
    }

    private record GetUserEventsAsStringCallable(
            String username, String startRangeDateTime, String endRangeDateTime) implements Callable<List<List<String>>> {
        @Override
        public List<List<String>> call() {
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
                statement.close();
                con.close();
                return userEventsList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

    public static List<Entry<String>> getUserEntries(String username, LocalDateTime startRangeDateTime,
                                                     LocalDateTime endRangeDateTime) {
        Callable<List<List<String>>> callable = new GetUserEventsAsStringCallable(
                username, startRangeDateTime.format(databaseDateTimeFormatter), endRangeDateTime.format(databaseDateTimeFormatter));
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        Future<List<List<String>>> future = executor.submit(callable);
        executor.shutdown();
        List<List<String>> userEventsAsString = new ArrayList<>();
        try {
            userEventsAsString = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        List<Entry<String>> userEntries = new ArrayList<>();
        for (List<String> eventAsString : userEventsAsString) {
            String title = eventAsString.get(0);
            LocalDateTime startDateTime = LocalDateTime.parse(eventAsString.get(1), databaseDateTimeFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(eventAsString.get(2), databaseDateTimeFormatter);
            boolean fullDay = Integer.parseInt(eventAsString.get(3)) == 1;
            //boolean recurring = Integer.parseInt(eventAsString.get(4)) == 1;
            String rrule = eventAsString.get(5).equals("") ? null : eventAsString.get(5);

            Entry<String> entry = new Entry<>(title, new Interval(startDateTime, endDateTime));
            entry.setFullDay(fullDay);
            entry.setRecurrenceRule(rrule);
            userEntries.add(entry);
        }
        return userEntries;
    }


    public static void removeEvent(Entry<?> entry, String username) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC DeleteRowFromEvents @title = ?, @user = ?, @startDateTime = ?, @endDateTime = ?");
                    statement.setString(1, event.getTitle());
                    statement.setString(2, username);
                    statement.setString(3, event.getStartDateTimeString());
                    statement.setString(4, event.getEndDateTimeString());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }

    public static void changeEventTitle(String oldTitle, Entry<?> entry, String username) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC ChangeEventTitle @user = ?, @oldTitle = ?, @newTitle = ?, @startDateTime = ?, @endDateTime = ?");
                    statement.setString(1, username);
                    statement.setString(2, oldTitle);
                    statement.setString(3, event.getTitle());
                    statement.setString(4, event.getStartDateTimeString());
                    statement.setString(5, event.getEndDateTimeString());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }

    public static void changeEventInterval(Interval oldInterval, Entry<?> entry, String username) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC ChangeEventInterval @user = ?, @title = ?, @oldStartDateTime = ?, @oldEndDateTime = ?, @newStartDateTime = ?, @newEndDateTime = ?");
                    statement.setString(1, username);
                    statement.setString(2, event.getTitle());
                    statement.setString(3, oldInterval.getStartDateTime().format(databaseDateTimeFormatter));
                    statement.setString(4, oldInterval.getEndDateTime().format(databaseDateTimeFormatter));
                    statement.setString(5, event.getStartDateTimeString());
                    statement.setString(6, event.getEndDateTimeString());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }
    public static void changeEventFullDay(boolean oldFullDay, Entry<?> entry, String username) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC ChangeEventFullDay @user = ?, @title = ?, @startDateTime = ?, @endDateTime = ?, @oldFullDay = ?, @newFullDay = ?");
                    statement.setString(1, username);
                    statement.setString(2, event.getTitle());
                    statement.setString(3, event.getStartDateTimeString());
                    statement.setString(4, event.getEndDateTimeString());
                    statement.setBoolean(5,oldFullDay);
                    statement.setBoolean(6, event.isFullDay());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }
    public static void ChangeEventRecurringAndRrule(Entry<?> entry, String username) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Connection con = DriverManager.getConnection(connectionString);
                    PreparedStatement statement = con.prepareStatement("EXEC ChangeEventRecurringAndRrule @user = ?, @title = ?, @startDateTime = ?, @endDateTime = ?, @recurring = ?, @rrule = ?");
                    statement.setString(1, username);
                    statement.setString(2, event.getTitle());
                    statement.setString(3, event.getStartDateTimeString());
                    statement.setString(4, event.getEndDateTimeString());
                    statement.setBoolean(5, event.isRecurring());
                    statement.setString(6, event.getRrule());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, 5, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
