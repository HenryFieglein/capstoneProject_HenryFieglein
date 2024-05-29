package com.kenzie.capstone.service.model;

import java.util.Objects;

public class GuestData {
    private String id;
    private String name;
    private String eventId;

    public GuestData(String id, String name, String eventId) {
        this.id = id;
        this.name = name;
        this.eventId = eventId;
    }

    public GuestData() {}

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
        GuestData data = (GuestData) o;
        return Objects.equals(id, data.id) && Objects.equals(name, data.name) && Objects.equals(eventId, data.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventId);
    }
}
