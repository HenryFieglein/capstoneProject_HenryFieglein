package com.kenzie.capstone.service.model;

import java.util.Objects;

public class Guest {
    private String id;
    private String name;
    private String eventId;

    public Guest(String id, String name, String eventId) {
        this.id = id;
        this.name = name;
        this.eventId = eventId;
    }

    public Guest() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(id, guest.id) && Objects.equals(name, guest.name) && Objects.equals(eventId, guest.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventId);
    }
}
