package com.app.app;

import com.app.loginapp.Database;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
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

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }


    public Event(String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.title=title; this.startDateTime=startTime; this.endDateTime=endTime;
    }

    public void addEventToDatabase(String username) {
        try {
            Database.addEvent(title, username, startDateTime, endDateTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Entry<String> getEntry() {
        return new Entry<>(title, new Interval(startDateTime, endDateTime));
    }

    public List<Event> getUserEventsFromDatabase(String username) throws UserEventsEmptyException {
        List<Event> userEvents = new ArrayList<>();
        List<List<String>> userEventsAsString = Database.getUserEventsAsString(username);
        if (userEventsAsString.isEmpty())
            throw new UserEventsEmptyException("User doesn't have events in database");
        for (List<String> eventAsString: userEventsAsString) {
            String title = eventAsString.get(0);
            LocalDateTime startDateTime = LocalDateTime.parse(eventAsString.get(1));
            LocalDateTime endDateTime = LocalDateTime.parse(eventAsString.get(2));
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
            LocalDateTime startDateTime = LocalDateTime.parse(eventAsString.get(1));
            LocalDateTime endDateTime = LocalDateTime.parse(eventAsString.get(2));
            userEntries.add(new Entry<>(title, new Interval(startDateTime, endDateTime)));
        }
        return userEntries;
    }

}
