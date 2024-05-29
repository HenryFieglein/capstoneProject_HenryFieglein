package com.kenzie.appserver.service.model;

import java.util.Objects;

public class Event {
    private String id;
    private String eventName;
    private String username;
    private String description;
    private String venueId;
    private String startDate;
    private String endDate;
    private String category;

    public Event() {
    }

    public Event(String id, String eventName, String username, String description, String venueId, String startDate, String endDate, String category) {
        if (id == null) {
            throw new IllegalArgumentException("Event id cannot be null");
        }
        if (eventName == null) {
            throw new IllegalArgumentException("Event name cannot be null");
        }
        if (username == null) {
            throw new IllegalArgumentException("Event username cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Event category cannot be null");
        }
        this.id = id;
        this.eventName = eventName;
        this.username = username;
        this.description = description;
        this.venueId = venueId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", eventName='" + eventName + '\'' +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", venueId='" + venueId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(eventName, event.eventName) && Objects.equals(username, event.username) && Objects.equals(description, event.description) && Objects.equals(venueId, event.venueId) && Objects.equals(startDate, event.startDate) && Objects.equals(endDate, event.endDate) && Objects.equals(category, event.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventName, username, description, venueId, startDate, endDate, category);
    }
}