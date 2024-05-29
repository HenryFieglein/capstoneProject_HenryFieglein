package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class EventCreateRequest {
    @NotEmpty
    @JsonProperty("event_name")
    private String eventName;

    @NotEmpty
    @JsonProperty("username")
    private String username;

    @NotEmpty
    @JsonProperty("venue_id")
    private String venueId;

    @NotEmpty
    @JsonProperty("description")
    private String description;

    @NotEmpty
    @JsonProperty("start_date")
    private String startDate;

    @NotEmpty
    @JsonProperty("end_date")
    private String endDate;

    @NotEmpty
    @JsonProperty("category")
    private String category;

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

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
