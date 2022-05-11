package com.app.app;

import java.time.LocalDateTime;

public class Event {
    static Event event = new Event();

    private String description;
    private EventType eventType;
    private LocalDateTime startTime, endTime;

    public enum EventType {
        SPORT,
        SHOPPING,
        LEARNING,
        FUN,
        OTHER
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    private Event() {}

    public static Event getInstance() {
        return event;
    }

    private void sendToDatabase() {

    }

    private void setEvent(EventType eventType, LocalDateTime startTime, LocalDateTime endTime, String description) {

    }

    private void getFromDatabase() {

    }

}
