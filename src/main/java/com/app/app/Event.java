package com.app.app;

import com.app.loginapp.Database;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import javafx.application.Platform;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Event {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static class UserEventsEmptyException extends Exception {
        public UserEventsEmptyException(String errorMessage) {
            super(errorMessage);
        }
    }
    private String title;
    private EventType eventType;
    private LocalDateTime startDateTime, endDateTime;

    public enum EventType {
        SPORT,
        SHOPPING,
        LEARNING,
        FUN,
        OTHER
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getStartDateTimeString() {
        return startDateTime.format(dateTimeFormatter);
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getEndDateTimeString() {
        return endDateTime.format(dateTimeFormatter);
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }


    public Event(String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.title=title; this.startDateTime=startTime; this.endDateTime=endTime;
    }
    public Event(String title) {
        this.title=title;
    }

    public Event(Entry<?> entry) {
        this.title=entry.getTitle();
        this.startDateTime=LocalDateTime.of(entry.getStartDate(), entry.getStartTime());
        this.endDateTime=LocalDateTime.of(entry.getEndDate(), entry.getEndTime());
    }

    public void addEventToDatabase(String username) {
        addEventToDatabase(this, username);
    }

    public void addEventToDatabase(Event event, String username) {
        try {
            Database.addEvent(event.getTitle(), username,
                    event.getStartDateTimeString(), event.getEndDateTimeString()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addEntryToDatabase(Entry<?> entry, String username) {
        Thread writeToDatabaseThread = new Thread(() -> Platform.runLater(() -> {
            Event event = Event.toEvent(entry);
            try {
                Database.addEvent(event.getTitle(), username,
                        event.getStartDateTimeString(), event.getEndDateTimeString()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        writeToDatabaseThread.setPriority(Thread.NORM_PRIORITY);
        writeToDatabaseThread.start();
    }

    public static void removeEntryFromDatabase(Entry<?> entry, String username) {
        Thread writeToDatabaseThread = new Thread(() -> Platform.runLater(() -> {
            Event event = Event.toEvent(entry);
            try {
                Database.deleteEvent(event.getTitle(), username,
                        event.getStartDateTimeString(), event.getEndDateTimeString()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        writeToDatabaseThread.setPriority(Thread.NORM_PRIORITY);
        writeToDatabaseThread.start();
    }

    public static void changeEntryTitleInDatabase(String oldTitle, Entry<?> entry, String username) {
        Thread writeToDatabaseThread = new Thread(() -> Platform.runLater(() -> {
            Event event = Event.toEvent(entry);
            try {
                Database.changeEventTitle(username, event.getTitle(), oldTitle,
                        event.getStartDateTimeString(), event.getEndDateTimeString()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        writeToDatabaseThread.setPriority(Thread.NORM_PRIORITY);
        writeToDatabaseThread.start();
    }
    public static void changeEntryIntervalInDatabase(Interval oldInterval, Entry<?> entry, String username) {
        Thread writeToDatabaseThread = new Thread(() -> Platform.runLater(() -> {
            Event event = Event.toEvent(entry);
            try {
                Database.changeEventInterval(username, event.getTitle(),
                        oldInterval.getStartDateTime().format(dateTimeFormatter),
                        oldInterval.getEndDateTime().format(dateTimeFormatter),
                        event.getStartDateTimeString(), event.getEndDateTimeString()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        writeToDatabaseThread.setPriority(Thread.NORM_PRIORITY);
        writeToDatabaseThread.start();
    }

    public Entry<String> toEntry() {
        return new Entry<>(title, new Interval(startDateTime, endDateTime));
    }

    public static Event toEvent(Entry<?> entry) {
        return new Event(entry);
    }

    public List<Event> getUserEventsFromDatabase(String username) throws UserEventsEmptyException {
        List<Event> userEvents = new ArrayList<>();
        List<List<String>> userEventsAsString = Database.getUserEventsAsString(username);
        if (userEventsAsString.isEmpty())
            throw new UserEventsEmptyException("User doesn't have events in database");
        for (List<String> eventAsString: userEventsAsString) {
            String title = eventAsString.get(0);
            LocalDateTime startDateTime = LocalDateTime.parse(eventAsString.get(1), dateTimeFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(eventAsString.get(2), dateTimeFormatter);
            userEvents.add(new Event(title, startDateTime, endDateTime));
        }
        return userEvents;
    }

    public List<Entry<String>> getUserEntriesFromDatabase(String username) throws UserEventsEmptyException {
        List<Entry<String>> userEntries = new ArrayList<>();
        List<List<String>> userEventsAsString = Database.getUserEventsAsString(username);
        if (userEventsAsString.isEmpty())
            throw new UserEventsEmptyException("User doesn't have events in database");
        for (List<String> eventAsString : userEventsAsString) {
            String title = eventAsString.get(0);
            LocalDateTime startDateTime = LocalDateTime.parse(eventAsString.get(1), dateTimeFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(eventAsString.get(2), dateTimeFormatter);
            userEntries.add(new Entry<>(title, new Interval(startDateTime, endDateTime)));
        }
        return userEntries;
    }

}
