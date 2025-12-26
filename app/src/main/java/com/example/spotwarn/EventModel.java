package com.example.spotwarn;

public class EventModel {
    private String eventName;
    private String date;

    public EventModel() {
        // Default constructor required for Firebase
    }

    public EventModel(String eventName, String date) {
        this.eventName = eventName;
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

