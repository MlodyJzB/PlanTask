package com.app.app;

import com.app.loginapp.Database;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Event {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    private String title, rrule;
    private LocalDateTime startDateTime, endDateTime;

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public boolean isFullDay() {
        return fullDay;
    }

    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    private boolean fullDay, recurring;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        this.fullDay=entry.isFullDay();
        this.recurring=entry.isRecurring();
        this.rrule=entry.getRecurrenceRule();
    }

    public void addEventToDatabase(String username) {
        addEventToDatabase(this, username);
    }

    public void addEventToDatabase(Event event, String username) {
        try {
            Database.addEvent(event.getTitle(), username,
                    event.getStartDateTimeString(), event.getEndDateTimeString(),
                    event.isFullDay()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Task<Void> addEntryToDatabase(Entry<?> entry, String username) {
        return new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Database.addEvent(event.getTitle(), username,
                            event.getStartDateTimeString(), event.getEndDateTimeString(),
                            event.isFullDay()
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

    }

    public static Task<Void> removeEntryFromDatabase(Entry<?> entry, String username) {
        return new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Database.deleteEvent(event.getTitle(), username,
                            event.getStartDateTimeString(), event.getEndDateTimeString()
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public static Task<Void> changeEntryTitleInDatabase(String oldTitle, Entry<?> entry, String username) {
        return new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Database.changeEventTitle(username, event.getTitle(), oldTitle,
                            event.getStartDateTimeString(), event.getEndDateTimeString()
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

    }
    public static Task<Void> changeEntryIntervalInDatabase(Interval oldInterval, Entry<?> entry, String username) {
        return new Task<>() {
            @Override
            public Void call() {
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
                return null;
            }
        };
    }

    public static Task<Void> changeEventFullDayInDatabase(boolean oldFullDay, Entry<?> entry, String username) {
        return new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Database.changeEventFullDay(username, event.getTitle(),
                            event.getStartDateTimeString(), event.getEndDateTimeString(),
                            oldFullDay, event.isFullDay()
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public static Task<Void> ChangeEventRecurringAndRruleInDatabase(Entry<?> entry, String username) {
        return new Task<>() {
            @Override
            public Void call() {
                Event event = Event.toEvent(entry);
                try {
                    Database.ChangeEventRecurringAndRrule(username, event.getTitle(),
                            event.getStartDateTimeString(), event.getEndDateTimeString(),
                            event.isRecurring(), event.getRrule()
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public Entry<String> toEntry() {
        return new Entry<>(title, new Interval(startDateTime, endDateTime));
    }

    public static Event toEvent(Entry<?> entry) {
        return new Event(entry);
    }


    public static List<Entry<String>> getUserEntriesFromDatabase(String username, LocalDateTime startRangeDateTime,
                                                          LocalDateTime endRangeDateTime) {
        List<Entry<String>> userEntries = new ArrayList<>();
        List<List<String>> userEventsAsString = Database.getUserEventsAsString(username,
                startRangeDateTime.format(dateTimeFormatter), endRangeDateTime.format(dateTimeFormatter)
        );
        for (List<String> eventAsString : userEventsAsString) {
            String title = eventAsString.get(0);
            LocalDateTime startDateTime = LocalDateTime.parse(eventAsString.get(1), dateTimeFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(eventAsString.get(2), dateTimeFormatter);
            boolean fullDay = Integer.parseInt(eventAsString.get(3)) == 1;
            boolean recurring = Integer.parseInt(eventAsString.get(4)) == 1;
            String rrule = eventAsString.get(5).equals("") ? null : eventAsString.get(5);

            Entry<String> entry = new Entry<>(title, new Interval(startDateTime, endDateTime));
            entry.setFullDay(fullDay);
            entry.setRecurrenceRule(rrule);
            userEntries.add(entry);
        }
        return userEntries;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
