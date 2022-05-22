package com.app.app;

import com.app.loginapp.Database;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        this.rrule=(entry.getRecurrenceRule()==null) ? "" : entry.getRecurrenceRule();
    }

    public Entry<String> toEntry() {
        return new Entry<>(title, new Interval(startDateTime, endDateTime));
    }

    public static Event toEvent(Entry<?> entry) {
        return new Event(entry);
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
